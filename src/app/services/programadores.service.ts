import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, forkJoin } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { UserProfile } from './autenticacion.service';
import { ProyectosService } from './proyectos.service';
import { Project } from '../models/portfolio.model';
import { environment } from '../../environments/environment';

// Interfaz que incluye la lista de proyectos del programador
export interface PerfilProgramador extends UserProfile {
  proyectos: Project[];
}

// Gestion y recuperacion de datos de programadores
@Injectable({
  providedIn: 'root'
})
export class ProgramadoresService {

  private apiUrl = `${environment.apiUrl}/usuarios`; // Endpoint general para usuarios

  constructor(
    private http: HttpClient,
    private proyectosService: ProyectosService // Necesitamos este servicio para obtener los proyectos de un programador
  ) { }


  // OBTENER(): programadores para las tarjetas
  getAllProgramadores(): Observable<UserProfile[]> {
    return this.http.get<UserProfile[]>(this.apiUrl).pipe(
      map(users => users.filter(user => user.role === 'Programador')) // Filtrar por rol 'Programador'
    );
  }

  // OBTENER(): detalles completos del programador, incluyendo sus proyectos
  getProgramadorById(id: string): Observable<PerfilProgramador | null> {
    const userProfile$ = this.http.get<UserProfile>(`${this.apiUrl}/${id}`);
    const userProjects$ = this.proyectosService.getProyectosPorUsuario(id);

    return forkJoin([userProfile$, userProjects$]).pipe(
      map(([userProfile, userProjects]) => {
        if (!userProfile || userProfile.role !== 'Programador') {
          return null; // Si no existe el usuario o no es programador, devolvemos null
        }
        return {
          ...userProfile,
          proyectos: userProjects || [] // Asignamos los proyectos obtenidos
        } as PerfilProgramador;
      })
    );
  }
}
