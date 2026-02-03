package com.example.dto;

// Este es un "Data Transfer Object". Su único propósito es definir la estructura
// de los datos que el backend de Jakarta EE debe enviar a este endpoint.
// Spring Boot convertirá automáticamente el JSON entrante en un objeto de esta clase.
public class NuevaAsesoriaDTO {

    private String programadorEmail;
    private String solicitanteNombre;
    private String comentario;

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
