import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfile } from './autenticacion.service';
import { environment } from '../../environments/environment';
import { firstValueFrom } from 'rxjs';


// Gestion de usuarios administrativos
@Injectable({
  providedIn: 'root'
})
export class AdministradoresService {

  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) { }


  // OBTENCION() una lista de todos los perfiles de usuario. (UID incluido)
  getAllUsers(): Observable<UserProfile[]> {
    return this.http.get<UserProfile[]>(this.apiUrl);
  }


  // EDITAR() usuario existente por UID
  async updateUserProfile(uid: string, data: Partial<UserProfile>): Promise<void> {
    // Primero, obtenemos el perfil completo del usuario para asegurar que enviamos una entidad válida.
    const existingUser = await this.getUser(uid);

    // Fusionamos los datos existentes con los nuevos cambios.
    const updatedUser = { ...existingUser, ...data };

    // Enviamos el objeto de usuario completo y actualizado al backend.
    await firstValueFrom(this.http.put<void>(this.apiUrl, updatedUser));
  }


  // ELIMINAR() un usuario por UID
  async deleteUser(uid: string): Promise<void> {
    await firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${uid}`));
  }


  // OBTENER() el perfil de un usuario con UID
  async getUser(uid: string): Promise<UserProfile> {
    return await firstValueFrom(this.http.get<UserProfile>(`${this.apiUrl}/${uid}`));
  }
}
