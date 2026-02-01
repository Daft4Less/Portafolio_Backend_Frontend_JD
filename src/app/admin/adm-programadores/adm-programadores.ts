import { Component, OnInit, OnDestroy } from '@angular/core';
import { AdministradoresService } from '../../services/administradores.service';
import { UserProfile } from '../../services/autenticacion.service';
import { Observable, BehaviorSubject, Subject, startWith, switchMap, takeUntil } from 'rxjs';
import { map } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';

// Gestion de usuarios y programadores.
@Component({
  selector: 'app-adm-programadores',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './adm-programadores.html',
  styleUrl: './adm-programadores.scss',
})
export class AdmProgramadores implements OnInit, OnDestroy {

  allUsers$!: Observable<UserProfile[]>;
  programmers$!: Observable<UserProfile[]>;
  nonProgrammers$!: Observable<UserProfile[]>;

  private refreshUsers$ = new Subject<void>();
  private destroy$ = new Subject<void>();

  private selectedProgrammerSubject = new BehaviorSubject<UserProfile | null>(null);
  selectedProgrammer$ = this.selectedProgrammerSubject.asObservable();

  programmerForm!: FormGroup;

  showModal: boolean = false;

  roles = ['Administrador', 'Programador', 'Usuario normal'];

  constructor(
    private administradoresService: AdministradoresService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.programmerForm = this.fb.group({
      uid: ['', Validators.required],
      displayName: ['', Validators.required],
      email: [{ value: '', disabled: true }, Validators.required],
      photoURL: [''],
      role: ['Usuario normal', Validators.required],
      especialidad: [''],
      descripcion: [''],
      contacto: this.fb.group({
        github: [''],
        linkedin: [''],
        website: ['']
      })
    });

    // --- Data Loading Logic ---
    this.allUsers$ = this.refreshUsers$.pipe(
      startWith(null), // Emit immediately on subscription
      switchMap(() => this.administradoresService.getAllUsers()),
      takeUntil(this.destroy$)
    );

    this.programmers$ = this.allUsers$.pipe(
      map(users => users.filter(user => user.role === 'Programador'))
    );

    this.nonProgrammers$ = this.allUsers$.pipe(
      map(users => users.filter(user => user.role !== 'Programador'))
    );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  //Selecciona un programador de la lista y carga sus datos en el formulario de edición.
  selectProgrammer(programmer: UserProfile): void {
    this.selectedProgrammerSubject.next(programmer);

    try {
      // Rellena los campos principales del formulario.
      this.programmerForm.patchValue({
        uid: programmer.uid,
        displayName: programmer.displayName,
        email: programmer.email,
        photoURL: programmer.photoURL,
        role: programmer.role,
        especialidad: programmer.especialidad || '',
        descripcion: programmer.descripcion || '',
      });

      // Maneja el grupo anidado 'contacto' por separado.
      const contactFormGroup = this.programmerForm.get('contacto');
      if (contactFormGroup) {
        let contactInfo = { github: '', linkedin: '', website: '' };
        
        if (programmer.contacto) {
          // Si ya es un objeto, úsalo directamente.
          // La lógica de parseo no funcionó, así que asumimos que la API debe devolver un objeto.
          // Si no lo hace, el problema está en la respuesta del backend.
          contactInfo = {
            github: programmer.contacto.github || '',
            linkedin: programmer.contacto.linkedin || '',
            website: programmer.contacto.website || ''
          };
        }
        
        contactFormGroup.patchValue(contactInfo);
      }
    } catch (error) {
      console.error('Error al poblar el formulario de programador:', error);
      alert('Error al cargar los datos del programador en el formulario. El modal se mostrará pero los campos pueden estar vacíos.');
    }
    this.showModal = true;
  }

  async saveProgrammer(): Promise<void> {
    if (this.programmerForm.valid) {
      const { uid, email, ...formData } = this.programmerForm.getRawValue();
      const programmerData: Partial<UserProfile> = {
        ...formData,
        email: email
      };

      try {
        await this.administradoresService.updateUserProfile(uid, programmerData);
        alert('Programador actualizado exitosamente.');
        this.refreshUsers$.next(); // Refresh the list
        this.closeModal();
      } catch (error) {
        console.error('Error al actualizar programador:', error);
        alert('Error al actualizar programador.');
      }
    }
  }

  async deleteProgrammer(uid: string): Promise<void> {
    if (confirm('¿Estás seguro de que quieres eliminar este programador?')) {
      try {
        await this.administradoresService.deleteUser(uid);
        alert('Programador eliminado exitosamente.');
        this.refreshUsers$.next(); // Refresh the list
        this.closeModal();
      } catch (error) {
        console.error('Error al eliminar programador:', error);
        alert('Error al eliminar programador.');
      }
    }
  }

  async promoteToProgrammer(user: UserProfile): Promise<void> {
    if (confirm(`¿Estás seguro de que quieres promover a ${user.displayName} a Programador?`)) {
      try {
        await this.administradoresService.updateUserProfile(user.uid, { role: 'Programador' });
        this.refreshUsers$.next(); // Refresh the list
      } catch (error) {
        console.error('Error al promover a programador:', error);
        alert('Error al promover a programador.');
      }
    }
  }

  async demoteFromProgrammer(user: UserProfile): Promise<void> {
    if (confirm(`¿Estás seguro de que quieres degradar a ${user.displayName} de Programador a Usuario normal?`)) {
      try {
        await this.administradoresService.updateUserProfile(user.uid, { role: 'Usuario normal' });
        alert(`${user.displayName} ha sido degradado a Usuario normal.`);
        this.refreshUsers$.next(); // Refresh the list
        this.closeModal();
      } catch (error) {
        console.error('Error al degradar a programador:', error);
        alert('Error al degradar a programador.');
      }
    }
  }

  closeModal(): void {
    this.selectedProgrammerSubject.next(null);
    this.programmerForm.reset();
    this.showModal = false;
  }
}

