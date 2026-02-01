import { UserProfile } from "../services/autenticacion.service";

export interface ProgrammerSchedule {
  id?: string;
  usuario?: Partial<UserProfile>; // Add user relationship
  dayOfWeek: number;
  startTime: string;
  endTime: string;
  isAvailable: boolean;
  startDateOffService?: string;
  endDateOffService?: string;
}
