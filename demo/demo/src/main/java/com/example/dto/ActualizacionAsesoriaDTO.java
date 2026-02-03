package com.example.dto;

// Este DTO se usará cuando el programador actualice el estado de una asesoría.
// Contiene los datos necesarios para notificar al usuario solicitante sobre el cambio.
public class ActualizacionAsesoriaDTO {

    private String solicitanteEmail;
    private String solicitanteNombre;
    private String comentario; // El comentario original para dar contexto
    private String nuevoEstado; // ej. "Aceptada", "Rechazada", "Finalizada"

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
