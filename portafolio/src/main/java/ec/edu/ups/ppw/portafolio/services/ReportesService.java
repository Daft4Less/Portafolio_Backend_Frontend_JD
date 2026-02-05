package ec.edu.ups.ppw.portafolio.services;

import ec.edu.ups.ppw.portafolio.business.GestionReportes;
import ec.edu.ups.ppw.portafolio.model.Asesoria;
import ec.edu.ups.ppw.portafolio.model.Proyecto;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("reportes")
public class ReportesService {

    @Inject
    private GestionReportes gestionReportes;

    @GET
    @Path("asesorias")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReporteAsesorias(
            @QueryParam("programadorId") String programadorId,
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("estado") String estado) {
        try {
            LocalDateTime fechaInicio = null;
            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr);
            }

            LocalDateTime fechaFin = null;
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr);
            }

            List<Asesoria> asesorias = gestionReportes.getReporteAsesorias(programadorId, fechaInicio, fechaFin, estado);
            return Response.ok(asesorias).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new Error(400, "Formato de fecha inválido", "El formato de fecha debe ser yyyy-MM-ddTHH:mm:ss. Error: " + e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno del servidor", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("proyectos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReporteProyectos(
            @QueryParam("usuarioId") String usuarioId) {
        try {
            if (usuarioId == null || usuarioId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new Error(400, "Parámetro requerido", "El 'usuarioId' es requerido para el reporte de proyectos."))
                        .build();
            }
            List<Proyecto> proyectos = gestionReportes.getReporteProyectosPorUsuario(usuarioId);
            return Response.ok(proyectos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno del servidor", e.getMessage()))
                    .build();
        }
    }
}
