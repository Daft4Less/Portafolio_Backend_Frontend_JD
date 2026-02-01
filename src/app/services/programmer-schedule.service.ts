import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, firstValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';
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

  // Mapea la respuesta del backend (con 'available') al modelo del frontend (con 'isAvailable').
  private mapBackendToFrontend(schedule: any): ProgrammerSchedule {
    if (!schedule) {
      return schedule;
    }
    return {
      ...schedule,
      isAvailable: schedule.available
    };
  }

  // OBTIENE los horarios de disponibilidad de un programador.
  getSchedules(programmerUid: string): Observable<ProgrammerSchedule[]> {
    if (!programmerUid) {
      return of([]);
    }
    const params = new HttpParams().set('usuarioId', programmerUid);
    return this.http.get<any[]>(this.apiUrl, { params }).pipe(
      map(schedulesFromBackend => 
        schedulesFromBackend.map(schedule => this.mapBackendToFrontend(schedule))
      )
    );
  }

  // AÑADE un nuevo horario para un programador.
  async addSchedule(programmerUid: string, schedule: Omit<ProgrammerSchedule, 'id'>): Promise<ProgrammerSchedule> {
    if (!programmerUid) {
      throw new Error('El ID del programador es requerido para añadir un horario.');
    }
    
    const backendPayload = {
      dayOfWeek: schedule.dayOfWeek,
      startTime: schedule.startTime,
      endTime: schedule.endTime,
      available: schedule.isAvailable,
      startDateOffService: schedule.startDateOffService || null,
      endDateOffService: schedule.endDateOffService || null,
      usuario: { id: programmerUid }
    };

    const params = new HttpParams().set('usuarioId', programmerUid);
    const createdSchedule = await firstValueFrom(this.http.post<any>(this.apiUrl, backendPayload, { params }));
    return this.mapBackendToFrontend(createdSchedule); // Mapea la respuesta
  }

  // ACTUALIZA un horario existente de un programador.
  async updateSchedule(programmerUid: string, scheduleId: string, schedule: Partial<ProgrammerSchedule>): Promise<ProgrammerSchedule> {
    if (!programmerUid) {
      throw new Error('El ID del programador es requerido para actualizar un horario.');
    }

    const backendPayload: any = { 
      id: scheduleId, 
      usuario: { id: programmerUid } 
    };

    if (schedule.dayOfWeek !== undefined) backendPayload.dayOfWeek = schedule.dayOfWeek;
    if (schedule.startTime !== undefined) backendPayload.startTime = schedule.startTime;
    if (schedule.endTime !== undefined) backendPayload.endTime = schedule.endTime;
    if (schedule.isAvailable !== undefined) backendPayload.available = schedule.isAvailable;
    if (schedule.startDateOffService !== undefined) backendPayload.startDateOffService = schedule.startDateOffService || null;
    if (schedule.endDateOffService !== undefined) backendPayload.endDateOffService = schedule.endDateOffService || null;

    const params = new HttpParams().set('usuarioId', programmerUid);
    const updatedSchedule = await firstValueFrom(this.http.put<any>(this.apiUrl, backendPayload, { params }));
    return this.mapBackendToFrontend(updatedSchedule); // Mapea la respuesta
  }

  // ELIMINA un horario por su ID.
  async deleteSchedule(programmerUid: string, scheduleId: string): Promise<void> {
    await firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${scheduleId}`));
  }
}
