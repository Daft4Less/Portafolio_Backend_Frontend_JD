import { Component, OnInit } from '@angular/core'; // Importar OnInit
import { CommonModule } from '@angular/common'; // Importar CommonModule
import { FormsModule } from '@angular/forms'; // Importar FormsModule
import { ReportesService } from '../../services/reportes.service'; // Ajustar la ruta
import { Project } from '../../models/portfolio.model'; // Ajustar la ruta
import { ProgramadoresService } from '../../services/programadores.service'; // Importar ProgramadoresService
import { UserProfile } from '../../services/autenticacion.service'; // Importar UserProfile

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reporte-proyectos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-proyectos.html',
  styleUrl: './reporte-proyectos.scss',
})
export class ReporteProyectosComponent implements OnInit { // Implementar OnInit
  programadores: UserProfile[] = []; // Lista de programadores para el desplegable
  selectedProgramadorId: string = ''; // ID del programador seleccionado
  proyectos: Project[] = []; // Proyectos cargados del programador seleccionado
  proyectosFiltrados: Project[] = []; // Proyectos después de aplicar filtros secundarios

  // Propiedades para filtros secundarios
  tipoParticipacionFiltro: '' | 'Frontend' | 'Backend' | 'Base de Datos' | 'Fullstack' = '';
  seccionFiltro: '' | 'Proyectos Académicos' | 'Proyectos Laborales' = '';

  constructor(
    private reportesService: ReportesService,
    private programadoresService: ProgramadoresService // Inyectar ProgramadoresService
  ) {}

  ngOnInit(): void {
    this.programadoresService.getAllProgramadores().subscribe({
      next: (data) => {
        this.programadores = data;
      },
      error: (err) => {
        console.error('Error al cargar programadores', err);
      }
    });
  }

  onProgramadorSeleccionado(): void {
    if (!this.selectedProgramadorId) {
      this.proyectos = [];
      this.proyectosFiltrados = [];
      this.tipoParticipacionFiltro = '';
      this.seccionFiltro = '';
      return;
    }

    this.reportesService.getReporteProyectos(this.selectedProgramadorId).subscribe({
      next: (data) => {
        this.proyectos = data;
        // Resetear filtros secundarios al cargar nuevos proyectos de un programador diferente
        this.tipoParticipacionFiltro = '';
        this.seccionFiltro = '';
        this.aplicarFiltrosSecundarios(); // Aplicar filtros (que ahora estarán reseteados)
      },
      error: (err) => {
        console.error('Error al cargar proyectos para el programador', err);
        this.proyectos = [];
        this.proyectosFiltrados = [];
      }
    });
  }

  aplicarFiltrosSecundarios(): void {
    let tempProyectos = [...this.proyectos];

    if (this.tipoParticipacionFiltro !== '') {
      tempProyectos = tempProyectos.filter(p => p.participationType === this.tipoParticipacionFiltro);
    }
    if (this.seccionFiltro !== '') {
      tempProyectos = tempProyectos.filter(p => p.section === this.seccionFiltro);
    }
    this.proyectosFiltrados = tempProyectos;
  }

  descargarPDFGeneral(): void {
    const doc = new jsPDF();
    const selectedProgramador = this.programadores.find(p => p.uid === this.selectedProgramadorId);
    const programadorName = selectedProgramador ? selectedProgramador.displayName : 'Desconocido';
    let y = 20; // Posición Y inicial
    const pageHeight = doc.internal.pageSize.height;
    const bottomMargin = 20; // Margen inferior

    // Añade el título y la fecha
    doc.setFontSize(18);
    doc.text('Reporte de Proyectos', 14, y);
    y += 8;
    doc.setFontSize(11);
    doc.setTextColor(100);
    doc.text(`Programador: ${programadorName}`, 14, y);
    y += 6;
    doc.text(`Fecha: ${new Date().toLocaleDateString()}`, 14, y);
    y += 12;

    const addField = (label: string, value: string | undefined | null) => {
      if (y > pageHeight - bottomMargin) { // Comprobar si se necesita nueva página
        doc.addPage();
        y = 20;
      }
      if (!value) return;
      doc.setFont('helvetica', 'bold');
      doc.text(`${label}:`, 14, y);
      doc.setFont('helvetica', 'normal');
      doc.text(value, 50, y, { maxWidth: 145 });
      y += 8; // Incrementar Y para la siguiente línea
    };

    this.proyectosFiltrados.forEach((proyecto, index) => {
      // Estimar altura del bloque del proyecto, si es muy grande, no cabe
      const estimatedHeight = 60 + (proyecto.description?.length || 0) / 4; // Estimación simple
      if (y + estimatedHeight > pageHeight - bottomMargin) {
        doc.addPage();
        y = 20; // Resetear Y en la nueva página
      }
      
      doc.setLineWidth(0.5);
      doc.line(14, y, 196, y); // Línea horizontal separadora
      y += 8;

      doc.setFontSize(14);
      doc.setFont('helvetica', 'bold');
      doc.text(proyecto.name, 14, y);
      doc.setFontSize(12);
      y += 10;
      
      addField('ID', proyecto.id?.substring(0, 8) || 'N/A');
      addField('Descripción', proyecto.description);
      addField('Participación', proyecto.participationType);
      addField('Sección', proyecto.section);
      if (proyecto.technologies && proyecto.technologies.length > 0) {
        addField('Tecnologías', proyecto.technologies.join(', '));
      }
      addField('Repositorio', proyecto.repositoryLink);
      addField('Despliegue', proyecto.deploymentLink);
      y += 5; // Espacio extra después de cada proyecto
    });

    doc.save(`reporte_proyectos_${programadorName.replace(/\s/g, '_')}.pdf`);
  }

  descargarPDFIndividual(proyecto: Project): void {
    const doc = new jsPDF();
    let y = 20; // Posición Y inicial

    doc.setFontSize(18);
    doc.text('Detalle del Proyecto', 14, y);
    y += 10;

    doc.setLineWidth(0.5);
    doc.line(14, y, 196, y); // Línea horizontal
    y += 10;

    doc.setFontSize(12);

    const addField = (label: string, value: string | undefined | null) => {
      if (!value) return;
      doc.setFont('helvetica', 'bold');
      doc.text(`${label}:`, 14, y);
      doc.setFont('helvetica', 'normal');
      doc.text(value, 50, y, { maxWidth: 145 }); // Ajuste de texto largo
      y += 10; // Incrementar Y para la siguiente línea
    };
    
    addField('ID', proyecto.id);
    addField('Nombre', proyecto.name);
    addField('Descripción', proyecto.description);
    addField('Participación', proyecto.participationType);
    addField('Sección', proyecto.section);
    if (proyecto.technologies && proyecto.technologies.length > 0) {
      addField('Tecnologías', proyecto.technologies.join(', '));
    }
    if (proyecto.repositoryLink) {
      addField('Repositorio', proyecto.repositoryLink);
    }
    if (proyecto.deploymentLink) {
      addField('Enlace Despliegue', proyecto.deploymentLink);
    }
    
    doc.save(`proyecto_${proyecto.name.replace(/\s/g, '_')}.pdf`);
  }
}
