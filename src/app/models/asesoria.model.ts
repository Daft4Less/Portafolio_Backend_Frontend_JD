import { UserProfile } from '../services/autenticacion.service';

export interface Asesoria {
  id?: string;
  solicitante: Partial<UserProfile>; // Cambiado de solicitanteId a un objeto
  solicitanteNombre: string;
  programador: Partial<UserProfile>; // Cambiado de programadorId a un objeto

  fecha: string; // Cambiado de Timestamp a string (formato ISO)
  comentario: string;
  estado: 'pendiente' | 'aprobada' | 'rechazada' | 'finalizada';
  respuestaProgramador?: string;
}
