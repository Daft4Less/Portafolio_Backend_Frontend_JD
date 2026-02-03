import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PruebaConexionService } from '../../services/prueba-conexion.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-prueba-conexion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './prueba-conexion.html',
  styleUrls: ['./prueba-conexion.scss']
})
export class PruebaConexionComponent implements OnInit {
  
  asesorias$!: Observable<any>;
  proyectos$!: Observable<any>;
  usuarios$!: Observable<any>;
  error: any;

  constructor(private pruebaConexionService: PruebaConexionService) { }

  ngOnInit(): void {
    this.asesorias$ = this.pruebaConexionService.getAsesorias();
    this.proyectos$ = this.pruebaConexionService.getProyectos();
    this.usuarios$ = this.pruebaConexionService.getUsuarios();

    this.asesorias$.subscribe({ error: err => this.error = err });
    this.proyectos$.subscribe({ error: err => this.error = err });
    this.usuarios$.subscribe({ error: err => this.error = err });
  }
}
