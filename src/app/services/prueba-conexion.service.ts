import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PruebaConexionService {
  private apiUrl = 'http://127.0.0.1:8080/portafolio/api';

  constructor(private http: HttpClient) { }

  getAsesorias(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/asesorias`);
  }

  getProyectos(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/proyectos`);
  }

  getUsuarios(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/usuarios`);
  }
}
