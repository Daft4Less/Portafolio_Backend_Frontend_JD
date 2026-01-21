package ec.edu.ups.ppw.portafolio.business;

import java.util.List;
import ec.edu.ups.ppw.portafolio.dao.UsuarioDAO;
import ec.edu.ups.ppw.portafolio.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class GestionUsuarios {

    @Inject
    private UsuarioDAO daoUsuario;

    public void guardarUsuarios(Usuario usuario) {
        Usuario u = daoUsuario.read(usuario.getUid());
        if (u == null) {
            daoUsuario.insert(usuario);
        } else {
            daoUsuario.update(usuario);
        }
    }

    public List<Usuario> getUsuarios() {
        return daoUsuario.getAll();
    }

    public Usuario getUsuario(String id) {
        return daoUsuario.read(id);
    }
    
    public void eliminarUsuario(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new Exception("ID de usuario no puede ser nulo o vacío.");
        }
        Usuario usuario = daoUsuario.read(id);
        if (usuario == null) {
            throw new Exception("Usuario a eliminar no encontrado.");
        }
        daoUsuario.delete(id);
    }
}
