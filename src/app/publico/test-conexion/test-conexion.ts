import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConexionTestService } from '../../services/conexion-test.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-test-conexion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './test-conexion.html',
  styleUrls: ['./test-conexion.scss']
})
export class TestConexionComponent implements OnInit {
  
  asesorias$!: Observable<any>;
  error: any;

  constructor(private conexionTestService: ConexionTestService) { }

  ngOnInit(): void {
    this.asesorias$ = this.conexionTestService.getAsesorias();
    this.asesorias$.subscribe({
      error: err => this.error = err
    });
  }
}
