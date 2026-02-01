import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, firstValueFrom } from 'rxjs';
import { ProgrammerSchedule } from '../models/programmer-schedule.model';
import { environment } from '../../environments/environment';

//Gestion de horario de disponibilidad de programadores
@Injectable({
  providedIn: 'root'
})
export class ProgrammerScheduleService {

  private apiUrl = `${environment.apiUrl}/horarios`;

  constructor(
    private http: HttpClient
  ) { }

  // OBTIENE los horarios de disponibilidad de un programador.
  getSchedules(programmerUid: string): Observable<ProgrammerSchedule[]> {
    if (!programmerUid) {
      return of([]);
    }
    const params = new HttpParams().set('usuarioId', programmerUid);
    return this.http.get<ProgrammerSchedule[]>(this.apiUrl, { params });
  }

  // AÑADE un nuevo horario para un programador.
  async addSchedule(programmerUid: string, schedule: Omit<ProgrammerSchedule, 'id'>): Promise<ProgrammerSchedule> {
    if (!programmerUid) {
      throw new Error('El ID del programador es requerido para añadir un horario.');
    }
    const params = new HttpParams().set('usuarioId', programmerUid);
    // El backend asigna el ID
    return await firstValueFrom(this.http.post<ProgrammerSchedule>(this.apiUrl, schedule, { params }));
  }

  // ACTUALIZA un horario existente de un programador.
  async updateSchedule(programmerUid: string, scheduleId: string, schedule: Partial<ProgrammerSchedule>): Promise<ProgrammerSchedule> {
    if (!programmerUid) {
      throw new Error('El ID del programador es requerido para actualizar un horario.');
    }
    const scheduleToUpdate = { ...schedule, id: scheduleId };
    const params = new HttpParams().set('usuarioId', programmerUid);
    return await firstValueFrom(this.http.put<ProgrammerSchedule>(this.apiUrl, scheduleToUpdate, { params }));
  }

  // ELIMINA un horario por su ID.
  async deleteSchedule(programmerUid: string, scheduleId: string): Promise<void> {
    // La API de backend para eliminar horarios solo necesita el scheduleId en la ruta.
    // programmerUid no es necesario para este endpoint específico, pero se mantiene en la firma si se usa en otros lugares.
    await firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${scheduleId}`));
  }
}
