package ec.edu.ups.ppw.portafolio.business;

import java.util.List;
import ec.edu.ups.ppw.portafolio.dao.ProyectoDAO;
import ec.edu.ups.ppw.portafolio.dao.UsuarioDAO;
import ec.edu.ups.ppw.portafolio.model.Proyecto;
import ec.edu.ups.ppw.portafolio.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.UUID;

@Stateless
public class GestionProyectos {

    @Inject
    private ProyectoDAO daoProyecto;
    
    @Inject
    private UsuarioDAO daoUsuario;

    public Proyecto guardarProyectos(Proyecto proyecto, String usuarioId) throws Exception {
        Usuario u = daoUsuario.read(usuarioId);
        if (u == null) {
            throw new Exception("Usuario no encontrado");
        }
        proyecto.setUsuario(u);

        if (proyecto.getId() == null || proyecto.getId().isEmpty()) {
            proyecto.setId(UUID.randomUUID().toString());
            daoProyecto.insert(proyecto);
        } else {
            Proyecto p = daoProyecto.read(proyecto.getId());
            if (p == null) {
                daoProyecto.insert(proyecto);
            } else {
                daoProyecto.update(proyecto);
            }
        }
        return proyecto;
    }

    public List<Proyecto> getProyectos() {
        return daoProyecto.getAll();
    }
    
    public List<Proyecto> getProyectosPorUsuario(String usuarioId) {
        return daoProyecto.getByUsuario(usuarioId);
    }

    public Proyecto getProyecto(String id) {
        return daoProyecto.read(id);
    }
    
    public void eliminarProyecto(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new Exception("ID de proyecto no puede ser nulo o vacío.");
        }
        Proyecto proyecto = daoProyecto.read(id);
        if (proyecto == null) {
            throw new Exception("Proyecto a eliminar no encontrado.");
        }
        daoProyecto.delete(id);
    }
}
