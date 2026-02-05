package ec.edu.ups.ppw.portafolio.business;

import ec.edu.ups.ppw.portafolio.dao.AsesoriaDAO;
import ec.edu.ups.ppw.portafolio.dao.ProyectoDAO;
import ec.edu.ups.ppw.portafolio.model.Asesoria;
import ec.edu.ups.ppw.portafolio.model.Proyecto;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class GestionReportes {

    @Inject
    private AsesoriaDAO daoAsesoria;

    @Inject
    private ProyectoDAO daoProyecto;

    // Método para obtener el reporte de asesorías
    public List<Asesoria> getReporteAsesorias(String programadorUid, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado) {
        return daoAsesoria.findAsesoriasByCriteria(programadorUid, fechaInicio, fechaFin, estado);
    }

    // Método para obtener el reporte de proyectos por usuario
    public List<Proyecto> getReporteProyectosPorUsuario(String usuarioUid) {
        return daoProyecto.getByUsuario(usuarioUid);
    }
}
