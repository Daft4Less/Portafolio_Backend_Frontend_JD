package ec.edu.ups.ppw.portafolio.services;

import java.net.URI;
import java.util.List;
import ec.edu.ups.ppw.portafolio.business.GestionUsuarios;
import ec.edu.ups.ppw.portafolio.model.Usuario;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("usuarios")
public class UsuariosService {

    @Inject
    private GestionUsuarios gu;

    @GET
    @Produces("application/json")
    public List<Usuario> getUsuarios() {
        return gu.getUsuarios();
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getUsuarioById(@PathParam("id") String id) {
        try {
            Usuario usuario = gu.getUsuario(id);
            if (usuario == null) {
                Error error = new Error(
                    404,
                    "No encontrado",
                    "Usuario con ID " + id + " no encontrado");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(error)
                        .build();
            }
            return Response.ok(usuario).build();
        } catch (Exception e) {
            e.printStackTrace();
            Error error = new Error(
                500,
                "Error interno",
                e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }

    @POST
    @Consumes("application/json")
    public Response crearUsuario(Usuario usuario, @Context UriInfo uriInfo) {
        try {
            gu.guardarUsuarios(usuario); // This handles both create and update
            URI location = uriInfo.getAbsolutePathBuilder()
                    .path(usuario.getUid())
                    .build();
            return Response.created(location).build(); // 201 Created with Location header, no body
        } catch (Exception e) {
            e.printStackTrace();
            Error error = new Error(
                500,
                "Error interno",
                e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response actualizarUsuario(Usuario usuario) {
        try {
            gu.guardarUsuarios(usuario); // This handles both create and update
            return Response.ok(usuario).build(); // 200 OK with updated user
        } catch (Exception e) {
            e.printStackTrace();
            Error error = new Error(
                500,
                "Error interno",
                e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response eliminarUsuario(@PathParam("id") String id) {
        try {
            gu.eliminarUsuario(id);
            return Response.noContent().build(); // 204 No Content
        } catch (Exception e) {
            e.printStackTrace();
            Error error = new Error(
                500,
                "Error interno",
                e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }
}
