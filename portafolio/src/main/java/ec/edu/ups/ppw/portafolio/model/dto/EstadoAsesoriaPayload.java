package ec.edu.ups.ppw.portafolio.model.dto;

public class EstadoAsesoriaPayload {
    private String solicitanteEmail;
    private String solicitanteNombre;
    private String comentario;
    private String nuevoEstado;

    public EstadoAsesoriaPayload(String solicitanteEmail, String solicitanteNombre, String comentario, String nuevoEstado) {
        this.solicitanteEmail = solicitanteEmail;
        this.solicitanteNombre = solicitanteNombre;
        this.comentario = comentario;
        this.nuevoEstado = nuevoEstado;
    }

    // Getters y Setters
    public String getSolicitanteEmail() {
        return solicitanteEmail;
    }

    public void setSolicitanteEmail(String solicitanteEmail) {
        this.solicitanteEmail = solicitanteEmail;
    }

    public String getSolicitanteNombre() {
        return solicitanteNombre;
    }

    public void setSolicitanteNombre(String solicitanteNombre) {
        this.solicitanteNombre = solicitanteNombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
}
