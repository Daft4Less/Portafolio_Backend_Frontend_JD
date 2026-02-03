package ec.edu.ups.ppw.portafolio.business;

import java.util.List;
import ec.edu.ups.ppw.portafolio.dao.AsesoriaDAO;
import ec.edu.ups.ppw.portafolio.dao.UsuarioDAO;
import ec.edu.ups.ppw.portafolio.model.Asesoria;
import ec.edu.ups.ppw.portafolio.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class GestionAsesorias {

    @Inject
    private AsesoriaDAO daoAsesoria;
    
    @Inject
    private UsuarioDAO daoUsuario;

    public void guardarAsesoria(Asesoria asesoria, String solicitanteId, String programadorId) throws Exception {
        Usuario solicitante = daoUsuario.read(solicitanteId);
        if (solicitante == null) {
            throw new Exception("Usuario solicitante no encontrado");
        }
        
        Usuario programador = daoUsuario.read(programadorId);
        if (programador == null) {
            throw new Exception("Usuario programador no encontrado");
        }

        asesoria.setSolicitante(solicitante);
        asesoria.setProgramador(programador);
        asesoria.setSolicitanteNombre(solicitante.getDisplayName()); // Asignamos el nombre
        
        Asesoria a = daoAsesoria.read(asesoria.getId());
        if (a == null) {
        	if(asesoria.getEstado() == null) {
        		asesoria.setEstado("pendiente"); // Estado por defecto al crear
        	}
            daoAsesoria.insert(asesoria);
        } else {
            daoAsesoria.update(asesoria);
        }
    }

    public List<Asesoria> getAsesorias() {
        return daoAsesoria.getAll();
    }
    
    public List<Asesoria> getAsesoriasPorSolicitante(String usuarioId) {
        return daoAsesoria.getBySolicitante(usuarioId);
    }
    
    public List<Asesoria> getAsesoriasPorProgramador(String usuarioId) {
        return daoAsesoria.getByProgramador(usuarioId);
    }

    public Asesoria getAsesoria(String id) {
        return daoAsesoria.read(id);
    }
    
    public void eliminarAsesoria(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new Exception("ID de asesoría no puede ser nulo o vacío.");
        }
        Asesoria asesoria = daoAsesoria.read(id);
        if (asesoria == null) {
            throw new Exception("Asesoría a eliminar no encontrada.");
        }
        daoAsesoria.delete(id);
    }
}
