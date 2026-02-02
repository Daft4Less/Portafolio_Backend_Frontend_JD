package ec.edu.ups.ppw.portafolio.business;

import java.util.List;

import ec.edu.ups.ppw.portafolio.dao.HorarioDAO;
import ec.edu.ups.ppw.portafolio.dao.UsuarioDAO;
import ec.edu.ups.ppw.portafolio.model.Horario;
import ec.edu.ups.ppw.portafolio.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class GestionHorarios {

    @Inject
    private HorarioDAO daoHorario;
    
    @Inject
    private UsuarioDAO daoUsuario;

    public void guardarHorario(Horario horario, String usuarioId) throws Exception {
        Usuario u = daoUsuario.read(usuarioId);
        if (u == null) {
            throw new Exception("Usuario no encontrado");
        }
        horario.setUsuario(u); // Asocia el horario al programador
        
        if (horario.getId() == null || horario.getId().isEmpty()) {
            horario.setId(java.util.UUID.randomUUID().toString());
            daoHorario.insert(horario);
        } else {
            daoHorario.update(horario);
        }
    }

    public List<Horario> getHorarios() {
        return daoHorario.getAll();
    }
    
    public List<Horario> getHorariosPorProgramador(String usuarioId) {
        return daoHorario.getByProgramador(usuarioId);
    }

    public Horario getHorario(String id) {
        return daoHorario.read(id);
    }
    
    public void eliminarHorario(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new Exception("ID de horario no puede ser nulo o vacío.");
        }
        Horario horario = daoHorario.read(id);
        if (horario == null) {
            throw new Exception("Horario a eliminar no encontrado.");
        }
        daoHorario.delete(id);
    }
}
