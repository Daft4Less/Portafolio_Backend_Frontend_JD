import { UserProfile } from '../services/autenticacion.service';

export interface Asesoria {
  id?: string;
  solicitante?: Partial<UserProfile>; 
  solicitanteId?: string;
  solicitanteNombre: string;
  programador?: Partial<UserProfile>; 
  programadorId?: string;

  fecha: string; // Cambiado de Timestamp a string (formato ISO)
  comentario: string;
  estado: 'pendiente' | 'aprobada' | 'rechazada' | 'finalizada';
  respuestaProgramador?: string;
}
