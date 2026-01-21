package ec.edu.ups.ppw.portafolio.services;

import java.net.URI;
import java.util.List;

import ec.edu.ups.ppw.portafolio.business.GestionAsesorias;
import ec.edu.ups.ppw.portafolio.model.Asesoria;
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

@Path("asesorias")
public class AsesoriaService {

    @Inject
    private GestionAsesorias ga;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Asesoria> getAsesorias(@QueryParam("solicitanteId") String solicitanteId, @QueryParam("programadorId") String programadorId) {
        if (solicitanteId != null && !solicitanteId.isEmpty()) {
            return ga.getAsesoriasPorSolicitante(solicitanteId);
        }
        if (programadorId != null && !programadorId.isEmpty()) {
            return ga.getAsesoriasPorProgramador(programadorId);
        }
        return ga.getAsesorias();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAsesoriaById(@PathParam("id") String id) {
        try {
            Asesoria asesoria = ga.getAsesoria(id);
            if (asesoria == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new Error(404, "No encontrado", "Asesoría con ID " + id + " no encontrada"))
                        .build();
            }
            return Response.ok(asesoria).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearAsesoria(Asesoria asesoria, @QueryParam("solicitanteId") String solicitanteId, @QueryParam("programadorId") String programadorId, @Context UriInfo uriInfo) {
        try {
            if (solicitanteId == null || solicitanteId.isEmpty() || programadorId == null || programadorId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new Error(400, "Petición incorrecta", "'solicitanteId' y 'programadorId' son requeridos."))
                        .build();
            }
            ga.guardarAsesoria(asesoria, solicitanteId, programadorId);
            URI location = uriInfo.getAbsolutePathBuilder().path(asesoria.getId()).build();
            return Response.created(location).entity(asesoria).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarAsesoria(Asesoria asesoria, @QueryParam("solicitanteId") String solicitanteId, @QueryParam("programadorId") String programadorId) {
        try {
            if (solicitanteId == null || solicitanteId.isEmpty() || programadorId == null || programadorId.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new Error(400, "Petición incorrecta", "'solicitanteId' y 'programadorId' son requeridos para actualizar."))
                        .build();
            }
            ga.guardarAsesoria(asesoria, solicitanteId, programadorId);
            return Response.ok(asesoria).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarAsesoria(@PathParam("id") String id) {
        try {
            ga.eliminarAsesoria(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(500, "Error interno", e.getMessage()))
                    .build();
        }
    }
}
