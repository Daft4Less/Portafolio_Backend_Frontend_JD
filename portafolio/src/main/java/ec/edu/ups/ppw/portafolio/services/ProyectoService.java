package ec.edu.ups.ppw.portafolio.services;

import java.net.URI;
import java.util.List;

import ec.edu.ups.ppw.portafolio.business.GestionProyectos;
import ec.edu.ups.ppw.portafolio.model.Proyecto;
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

@Path("proyectos")
public class ProyectoService {

    @Inject
    private GestionProyectos gp;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Proyecto> getProyectos(@QueryParam("usuarioId") String usuarioId) {
        if (usuarioId != null && !usuarioId.isEmpty()) {
            return gp.getProyectosPorUsuario(usuarioId);
        }
        return gp.getProyectos();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProyectoById(@PathParam("id") String id) {
        try {
            Proyecto proyecto = gp.getProyecto(id);
            if (proyecto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new Error(404, "No encontrado", "Proyecto con ID " + id + " no encontrado"))
                        .build();
            }
            return Response.ok(proyecto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearProyecto(Proyecto proyecto, @QueryParam("usuarioId") String usuarioId, @Context UriInfo uriInfo) {
        try {
            if (usuarioId == null || usuarioId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new Error(400, "Petición incorrecta", "El 'usuarioId' es requerido para crear un proyecto."))
                        .build();
            }
            gp.guardarProyectos(proyecto, usuarioId);
            URI location = uriInfo.getAbsolutePathBuilder().path(proyecto.getId()).build();
            return Response.created(location).entity(proyecto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarProyecto(Proyecto proyecto, @QueryParam("usuarioId") String usuarioId) {
        try {
            if (usuarioId == null || usuarioId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new Error(400, "Petición incorrecta", "El 'usuarioId' es requerido para actualizar un proyecto."))
                        .build();
            }
            gp.guardarProyectos(proyecto, usuarioId);
            return Response.ok(proyecto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarProyecto(@PathParam("id") String id) {
        try {
            gp.eliminarProyecto(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }
}
