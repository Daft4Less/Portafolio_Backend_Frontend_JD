package ec.edu.ups.ppw.portafolio.services;

import java.net.URI;
import java.util.List;

import ec.edu.ups.ppw.portafolio.business.GestionHorarios;
import ec.edu.ups.ppw.portafolio.model.Horario;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("horarios")
public class HorarioService {

    @Inject
    private GestionHorarios gh;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Horario> getHorarios(@QueryParam("usuarioId") String usuarioId) {
        if (usuarioId != null && !usuarioId.isEmpty()) {
            return gh.getHorariosPorProgramador(usuarioId);
        }
        return gh.getHorarios();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHorarioById(@PathParam("id") String id) {
        try {
            Horario horario = gh.getHorario(id);
            if (horario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new Error(404, "No encontrado", "Horario con ID " + id + " no encontrado"))
                        .build();
            }
            return Response.ok(horario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearHorario(Horario horario, @QueryParam("usuarioId") String usuarioId, @Context UriInfo uriInfo) {
        try {
            if (usuarioId == null || usuarioId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new Error(400, "Petición incorrecta", "El 'usuarioId' es requerido para crear un horario."))
                        .build();
            }
            gh.guardarHorario(horario, usuarioId);
            URI location = uriInfo.getAbsolutePathBuilder().path(horario.getId()).build();
            return Response.created(location).entity(horario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarHorario(Horario horario, @QueryParam("usuarioId") String usuarioId) {
        try {
            if (usuarioId == null || usuarioId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new Error(400, "Petición incorrecta", "El 'usuarioId' es requerido para actualizar un horario."))
                        .build();
            }
            gh.guardarHorario(horario, usuarioId);
            return Response.ok(horario).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarHorario(@PathParam("id") String id) {
        try {
            gh.eliminarHorario(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }
}
