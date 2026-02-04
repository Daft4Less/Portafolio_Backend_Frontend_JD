package ec.edu.ups.ppw.portafolio.business;

import java.util.List;
import ec.edu.ups.ppw.portafolio.dao.AsesoriaDAO;
import ec.edu.ups.ppw.portafolio.dao.UsuarioDAO;
import ec.edu.ups.ppw.portafolio.model.Asesoria;
import ec.edu.ups.ppw.portafolio.model.Usuario;
import ec.edu.ups.ppw.portafolio.model.dto.TelegramPayload;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class GestionAsesorias {

    @Inject
    private AsesoriaDAO daoAsesoria;
    
    @Inject
    private UsuarioDAO daoUsuario;
    
    @Inject
    private NotificationClient notificationClient;

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
            notificationClient.notificarNuevaAsesoria(asesoria);
            
            // Prueba de concepto para notificación de Telegram
            long chatIdParaPrueba = 6817315879L; // CHAT_ID REAL PARA PROBAR
            String mensajeTelegram = "¡Nueva Asesoría!\n\n" +
                                     "Solicitante: " + asesoria.getSolicitante().getDisplayName() + "\n" +
                                     "Programador: " + asesoria.getProgramador().getDisplayName() + "\n" +
                                     "Asunto: " + asesoria.getComentario();
            notificationClient.sendTelegramNotification(new TelegramPayload(chatIdParaPrueba, mensajeTelegram));
            
        } else {
            daoAsesoria.update(asesoria);
            notificationClient.notificarActualizacionEstado(asesoria);
            
            // Notificación de Telegram para actualización de estado
            long chatIdParaPrueba = 6817315879L; // Mismo CHAT_ID de prueba para el solicitante
            String mensajeTelegram = "¡Actualización de Estado!\n\n" +
                                     "Asesoría: " + asesoria.getComentario() + "\n" +
                                     "Nuevo Estado: " + asesoria.getEstado().toUpperCase();
            notificationClient.sendTelegramNotification(new TelegramPayload(chatIdParaPrueba, mensajeTelegram));
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
