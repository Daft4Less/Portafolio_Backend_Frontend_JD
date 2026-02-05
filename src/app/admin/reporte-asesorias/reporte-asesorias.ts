import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importar CommonModule
import { FormsModule } from '@angular/forms'; // Importar FormsModule
import { ReportesService } from '../../services/reportes.service'; // Ajustar la ruta
import { Asesoria } from '../../models/asesoria.model'; // Ajustar la ruta
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reporte-asesorias',
  standalone: true, // Marcar como standalone
  imports: [CommonModule, FormsModule], // Añadir CommonModule y FormsModule aquí
  templateUrl: './reporte-asesorias.html',
  styleUrl: './reporte-asesorias.scss',
})
export class ReporteAsesoriasComponent {
  programadorId: string = '';
  fechaInicio: string = '';
  fechaFin: string = '';
  estado: 'pendiente' | 'aprobada' | 'finalizada' | '' = '';
  asesorias: Asesoria[] = [];

  constructor(private reportesService: ReportesService) {}

  generarReporte(): void {
    // Asegurarse de que las fechas tengan el formato correcto si están presentes
    const formattedFechaInicio = this.fechaInicio ? new Date(this.fechaInicio).toISOString().slice(0, 19) : undefined;
    const formattedFechaFin = this.fechaFin ? new Date(this.fechaFin).toISOString().slice(0, 19) : undefined;

    this.reportesService.getReporteAsesorias(
      this.programadorId,
      formattedFechaInicio,
      formattedFechaFin,
      this.estado === '' ? undefined : this.estado
    ).subscribe({
      next: (data) => {
        this.asesorias = data;
      },
      error: (err) => {
        console.error('Error al generar reporte de asesorías', err);
        // Manejar el error, mostrar un mensaje al usuario, etc.
      }
    });
  }

  descargarPDF(): void {
    const doc = new jsPDF();
    autoTable(doc, {
      head: [['ID', 'Solicitante', 'Programador', 'Fecha', 'Estado', 'Comentario', 'Respuesta']],
      body: this.asesorias.map(asesoria => [
        String(asesoria.id || 'N/A'),
        String(asesoria.solicitante?.displayName || 'N/A'),
        String(asesoria.programador?.displayName || 'N/A'),
        String(asesoria.fecha || 'N/A'),
        String(asesoria.estado || 'N/A'),
        String(asesoria.comentario || 'N/A'),
        String(asesoria.respuestaProgramador || 'N/A')
      ]),
    });
    doc.save('reporte_asesorias.pdf');
  }
}

