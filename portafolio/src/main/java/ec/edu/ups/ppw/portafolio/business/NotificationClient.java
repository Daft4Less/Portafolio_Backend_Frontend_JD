package ec.edu.ups.ppw.portafolio.business;

import ec.edu.ups.ppw.portafolio.model.Asesoria;
import ec.edu.ups.ppw.portafolio.model.dto.EstadoAsesoriaPayload;
import ec.edu.ups.ppw.portafolio.model.dto.NuevaAsesoriaPayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
public class NotificationClient {

    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8081/api/notificaciones";

    private Client client = ClientBuilder.newClient();

    public void notificarNuevaAsesoria(Asesoria asesoria) {
        NuevaAsesoriaPayload payload = new NuevaAsesoriaPayload(
            asesoria.getProgramador().getEmail(),
            asesoria.getSolicitante().getDisplayName(),
            asesoria.getComentario()
        );

        try {
            client.target(NOTIFICATION_SERVICE_URL + "/nueva-asesoria")
                  .request(MediaType.APPLICATION_JSON)
                  .post(Entity.json(payload));
            System.out.println("Notificación de nueva asesoría enviada.");
        } catch (Exception e) {
            System.err.println("Error al enviar notificación de nueva asesoría: " + e.getMessage());
            // No relanzar la excepción para no revertir la transacción principal
        }
    }

    public void notificarActualizacionEstado(Asesoria asesoria) {
        EstadoAsesoriaPayload payload = new EstadoAsesoriaPayload(
            asesoria.getSolicitante().getEmail(),
            asesoria.getSolicitante().getDisplayName(),
            asesoria.getComentario(),
            asesoria.getEstado()
        );

        try {
            client.target(NOTIFICATION_SERVICE_URL + "/estado-asesoria")
                  .request(MediaType.APPLICATION_JSON)
                  .post(Entity.json(payload));
            System.out.println("Notificación de actualización de estado enviada.");
        } catch (Exception e) {
            System.err.println("Error al enviar notificación de actualización de estado: " + e.getMessage());
            // No relanzar la excepción para no revertir la transacción principal
        }
    }
}
