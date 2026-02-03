import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, switchMap, firstValueFrom } from 'rxjs';
import { Project } from '../models/portfolio.model';
import { AutenticacionService } from './autenticacion.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProyectosService {

  private apiUrl = `${environment.apiUrl}/proyectos`;

  constructor(
    private http: HttpClient,
    private authService: AutenticacionService
  ) {}

  // OBTIENE los proyectos del usuario actualmente autenticado.
  getProyectos(): Observable<Project[]> {
    return this.authService.getUsuarioActual().pipe(
      switchMap(user => {
        if (!user) {
          return of([]); // Si no hay usuario, no hay proyectos.
        }
        const params = new HttpParams().set('usuarioId', user.uid);
        return this.http.get<Project[]>(this.apiUrl, { params });
      })
    );
  }

  // OBTIENE los proyectos de un usuario específico por su ID.
  getProyectosPorUsuario(usuarioId: string): Observable<Project[]> {
    if (!usuarioId) {
      return of([]);
    }
    const params = new HttpParams().set('usuarioId', usuarioId);
    return this.http.get<Project[]>(this.apiUrl, { params });
  }

  // AGREGA un nuevo proyecto para el usuario autenticado.
  async addProyecto(project: Omit<Project, 'id'>): Promise<Project> {
    const user = await firstValueFrom(this.authService.getUsuarioActual());
    if (!user) {
      throw new Error('Usuario no autenticado para agregar proyecto.');
    }

    const params = new HttpParams().set('usuarioId', user.uid);
    // El backend genera el ID, por lo que lo recibimos en la respuesta.
    return firstValueFrom(this.http.post<Project>(this.apiUrl, project, { params }));
  }

  // ACTUALIZA un proyecto existente para el usuario autenticado.
  async updateProyecto(projectId: string, datosActualizados: Partial<Project>): Promise<Project> {
    const user = await firstValueFrom(this.authService.getUsuarioActual());
    if (!user) {
      throw new Error('Usuario no autenticado para actualizar proyecto.');
    }

    const projectToUpdate = { ...datosActualizados, id: projectId };
    const params = new HttpParams().set('usuarioId', user.uid);
    return firstValueFrom(this.http.put<Project>(this.apiUrl, projectToUpdate, { params }));
  }

  // ELIMINA un proyecto por su ID.
  async deleteProyecto(projectId: string): Promise<void> {
    // La eliminación no requiere el usuarioId según la API, solo el ID del proyecto.
    await firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${projectId}`));
  }
}
