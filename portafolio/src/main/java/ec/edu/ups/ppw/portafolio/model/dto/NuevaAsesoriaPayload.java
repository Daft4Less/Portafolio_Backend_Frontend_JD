package ec.edu.ups.ppw.portafolio.model.dto;

public class NuevaAsesoriaPayload {
    private String programadorEmail;
    private String solicitanteNombre;
    private String comentario;

    public NuevaAsesoriaPayload(String programadorEmail, String solicitanteNombre, String comentario) {
        this.programadorEmail = programadorEmail;
        this.solicitanteNombre = solicitanteNombre;
        this.comentario = comentario;
    }

    // Getters y Setters
    public String getProgramadorEmail() {
        return programadorEmail;
    }

    public void setProgramadorEmail(String programadorEmail) {
        this.programadorEmail = programadorEmail;
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
}
