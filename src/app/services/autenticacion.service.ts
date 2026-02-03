import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Auth, GoogleAuthProvider, signInWithPopup, authState, User } from '@angular/fire/auth';
import { Observable, from, of, catchError, map, switchMap, firstValueFrom } from 'rxjs';

import { environment } from '../../environments/environment';
import { ProgrammerSchedule } from '../models/programmer-schedule.model';
import { Project } from '../models/portfolio.model';

// Definicion de la estructura de un perfil de usuario (matches backend Usuario.java)
export interface UserProfile {
  uid: string;
  email: string;
  displayName: string;
  photoURL: string;
  role: 'Administrador' | 'Programador' | 'Usuario normal';
  especialidad?: string;
  descripcion?: string;
  contacto?: any; // Assuming 'Contacto' is a simple object
  schedules?: ProgrammerSchedule[]; // These are likely not part of the base UserProfile anymore, but keeping for compatibility if needed elsewhere.
  projects?: Project[]; // Same as above.
}

// Servicio encargado de la autenticación y gestión de perfiles de usuario
@Injectable({
  providedIn: 'root'
})
export class AutenticacionService {
  private apiUrl = `${environment.apiUrl}/usuarios`;

  constructor(
    private auth: Auth,
    private http: HttpClient
  ) {}

  // REGISTRAR(): Autentica con Google y luego registra el usuario en nuestro backend.
  async registerWithGoogle(): Promise<UserProfile> {
    const provider = new GoogleAuthProvider();
    provider.setCustomParameters({ prompt: 'select_account' });

    const credential = await signInWithPopup(this.auth, provider);
    const user = credential.user;

    // Primero, verificamos si el usuario ya existe en nuestro backend.
    const userExists = await firstValueFrom(
      this.http.get<UserProfile>(`${this.apiUrl}/${user.uid}`).pipe(
        map(() => true), // Si obtenemos una respuesta, el usuario existe
        catchError(error => {
          if (error.status === 404) {
            return of(false); // 404 Not Found significa que el usuario no existe (lo cual es bueno para registrar)
          }
          throw error; // Lanza otros errores
        })
      )
    );

    if (userExists) {
      await this.auth.signOut();
      throw new Error('AUTH/USER-ALREADY-EXISTS');
    }

    // Si no existe, creamos el perfil en nuestro backend.
    const newUserProfile: UserProfile = {
      uid: user.uid,
      email: user.email!,
      displayName: user.displayName!,
      photoURL: user.photoURL!,
      role: 'Usuario normal'
    };

    // Usamos POST para crear el nuevo usuario en el backend
    return firstValueFrom(this.http.post<UserProfile>(this.apiUrl, newUserProfile));
  }

  // INICIAR SESIÓN(): Autentica con Google y obtiene el perfil de nuestro backend.
  async signInWithGoogle(): Promise<UserProfile> {
    const provider = new GoogleAuthProvider();
    provider.setCustomParameters({ prompt: 'select_account' });
    
    const credential = await signInWithPopup(this.auth, provider);
    const user = credential.user;

    // Hacemos GET a nuestro backend para obtener el perfil del usuario.
    return await firstValueFrom(
      this.http.get<UserProfile>(`${this.apiUrl}/${user.uid}`).pipe(
        catchError(async (error) => {
          if (error.status === 404) {
            // Si no se encuentra en nuestro backend, cerramos sesión y lanzamos error.
            await this.auth.signOut();
            throw new Error('AUTH/USER-NOT-FOUND');
          }
          throw error; // Lanza otros errores
        })
      )
    );
  }

  // Obtiene el perfil de nuestro backend para el usuario actualmente autenticado en Firebase.
  getUsuarioActual(): Observable<UserProfile | null> {
    return authState(this.auth).pipe(
      switchMap((user: User | null) => {
        if (user) {
          // Si hay un usuario de Firebase, obtenemos su perfil de nuestro backend
          return this.http.get<UserProfile>(`${this.apiUrl}/${user.uid}`).pipe(
            catchError(() => {
              // Si hay un error (ej. 404), el perfil no existe en el backend.
              // Devolvemos null para indicar que el usuario no está completamente configurado.
              return of(null);
            })
          );
        } else {
          // Si no hay usuario de Firebase, no hay usuario actual.
          return of(null);
        }
      })
    );
  }

  // Cierra la sesión del cliente de Firebase Auth.
  logout(): Promise<void> {
    return this.auth.signOut();
  }
}