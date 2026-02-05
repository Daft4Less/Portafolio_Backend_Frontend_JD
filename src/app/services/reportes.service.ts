import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Asesoria } from '../models/asesoria.model'; // Necesitas esta interfaz
import { Project } from '../models/portfolio.model'; // Necesitas esta interfaz

@Injectable({
  providedIn: 'root'
})
export class ReportesService {
  private apiUrl = `${environment.apiUrl}/reportes`;

  constructor(private http: HttpClient) { }

  /**
   * Obtiene un listado de asesorías filtrado.
   * @param programadorId UID del programador (opcional).
   * @param fechaInicio Fecha y hora de inicio en formato ISO 8601 (yyyy-MM-ddTHH:mm:ss) (opcional).
   * @param fechaFin Fecha y hora de fin en formato ISO 8601 (yyyy-MM-ddTHH:mm:ss) (opcional).
   * @param estado Estado de la asesoría (ej. "pendiente", "aprobada", "finalizada") (opcional).
   * @returns Observable con una lista de objetos Asesoria.
   */
  getReporteAsesorias(
    programadorId?: string,
    fechaInicio?: string,
    fechaFin?: string,
    estado?: 'pendiente' | 'aprobada' | 'finalizada'
  ): Observable<Asesoria[]> {
    let params = new HttpParams();
    if (programadorId) {
      params = params.set('programadorId', programadorId);
    }
    if (fechaInicio) {
      params = params.set('fechaInicio', fechaInicio);
    }
    if (fechaFin) {
      params = params.set('fechaFin', fechaFin);
    }
    if (estado) {
      params = params.set('estado', estado);
    }
    return this.http.get<Asesoria[]>(`${this.apiUrl}/asesorias`, { params });
  }

  /**
   * Obtiene un listado de proyectos por usuario.
   * @param usuarioId UID del usuario al que pertenecen los proyectos (obligatorio).
   * @returns Observable con una lista de objetos Project.
   */
  getReporteProyectos(usuarioId: string): Observable<Project[]> {
    let params = new HttpParams();
    params = params.set('usuarioId', usuarioId);
    return this.http.get<Project[]>(`${this.apiUrl}/proyectos`, { params });
  }
}
