import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, firstValueFrom } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';
import { Asesoria } from '../models/asesoria.model';
import { AutenticacionService } from './autenticacion.service';
import { environment } from '../../environments/environment';

// Gestión de asesorías entre usuarios y programadores en el backend
@Injectable({
  providedIn: 'root'
})
export class AsesoriasService {

  private apiUrl = `${environment.apiUrl}/asesorias`;

  constructor(
    private http: HttpClient,
    private authService: AutenticacionService
  ) { }

  // CREAR una nueva solicitud de asesoría en el backend.
  async addSolicitudAsesoria(solicitud: { programadorId: string; fecha: string; hora: string; comentario: string }): Promise<Asesoria> {
    const user = await firstValueFrom(this.authService.getUsuarioActual());
    if (!user) {
      throw new Error('Debes iniciar sesión para solicitar una asesoría.');
    }

    // Convertir fecha y hora a formato ISO 8601 para el backend (LocalDateTime)
    const fechaHoraISO = new Date(`${solicitud.fecha}T${solicitud.hora}:00`).toISOString(); // Añadimos :00 para segundos

    const nuevaAsesoria: Asesoria = {
      // id será asignado por el backend
      programador: { uid: solicitud.programadorId } as any, // Solo necesitamos el UID para la relación
      fecha: fechaHoraISO,
      comentario: solicitud.comentario,
      solicitante: { uid: user.uid } as any, // Solo necesitamos el UID para la relación
      solicitanteNombre: user.displayName!,
      estado: 'pendiente'
    };

    const params = new HttpParams()
      .set('solicitanteId', user.uid)
      .set('programadorId', solicitud.programadorId);

    return firstValueFrom(this.http.post<Asesoria>(this.apiUrl, nuevaAsesoria, { params }));
  }

  // OBTIENE las asesorías donde el usuario autenticado es el programador.
  getAsesorias(): Observable<Asesoria[]> {
    return this.authService.getUsuarioActual().pipe(
      switchMap(user => {
        if (!user || user.role !== 'Programador') {
          return of([]); // Solo los programadores pueden ver 'sus' asesorías por este método
        }
        return this.getAsesoriasParaProgramador(user.uid);
      })
    );
  }

  // ACTUALIZA el estado de una asesoría e incluye una respuesta del programador.
  async updateEstadoAsesoria(asesoriaId: string, estado: 'aprobada' | 'rechazada' | 'finalizada', respuesta?: string): Promise<Asesoria> {
    const user = await firstValueFrom(this.authService.getUsuarioActual());
    if (!user || user.role !== 'Programador') {
      throw new Error('Permiso denegado. Solo un programador puede modificar el estado de una asesoría.');
    }

    // Obtener la asesoría existente para obtener solicitanteId y programadorId
    const existingAsesoria = await firstValueFrom(this.getAsesoriaById(asesoriaId));
    if (!existingAsesoria) {
      throw new Error('Asesoría no encontrada.');
    }
    if (existingAsesoria.programador?.uid !== user.uid) { // Check if authenticated user is the programador
      throw new Error('Permiso denegado. No puedes modificar esta asesoría.');
    }

    const asesoriaToUpdate: Asesoria = {
      ...existingAsesoria,
      estado: estado,
      respuestaProgramador: respuesta || existingAsesoria.respuestaProgramador // Actualiza o mantiene la respuesta
    };

    const params = new HttpParams()
      .set('solicitanteId', existingAsesoria.solicitante?.uid || '') // Asumo que siempre habrá solicitante
      .set('programadorId', existingAsesoria.programador?.uid || ''); // Asumo que siempre habrá programador

    return await firstValueFrom(this.http.put<Asesoria>(this.apiUrl, asesoriaToUpdate, { params }));
  }

  // OBTIENE asesorías con el id del programador.
  getAsesoriasParaProgramador(programadorId: string): Observable<Asesoria[]> {
    if (!programadorId) {
      return of([]);
    }
    const params = new HttpParams().set('programadorId', programadorId);
    return this.http.get<Asesoria[]>(this.apiUrl, { params });
  }

  // OBTIENE asesorías con el id del usuario solicitante.
  getAsesoriasDeSolicitante(solicitanteId: string): Observable<Asesoria[]> {
    if (!solicitanteId) {
      return of([]);
    }
    const params = new HttpParams().set('solicitanteId', solicitanteId);
    return this.http.get<Asesoria[]>(this.apiUrl, { params });
  }

  // Método auxiliar para obtener una asesoría por ID (necesario para updateEstadoAsesoria)
  private getAsesoriaById(id: string): Observable<Asesoria | null> {
      return this.http.get<Asesoria>(`${this.apiUrl}/${id}`).pipe(
        catchError(() => of(null))
      );
  }

  // ELIMINA una asesoría por su ID.
  async deleteAsesoria(id: string): Promise<void> {
    await firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${id}`));
  }
}
