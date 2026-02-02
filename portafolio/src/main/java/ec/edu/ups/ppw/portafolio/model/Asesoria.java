package ec.edu.ups.ppw.portafolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@Table(name = "TBL_ASESORIA")
public class Asesoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ase_id")
    private String id;

    // Relación 1: Quien pide la cita
    @ManyToOne
    @JoinColumn(name = "solicitante_uid", nullable = false)
    private Usuario solicitante;

    // Guardamos el nombre del solicitante 
    @Column(name = "ase_solicitante_nombre")
    private String solicitanteNombre;

    // Relación 2: El programador experto
    @ManyToOne
    @JoinColumn(name = "programador_uid", nullable = false)
    private Usuario programador;

    @Column(name = "ase_fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "ase_comentario", nullable = false)
    private String comentario;

    @Column(name = "ase_estado", nullable = false)
    private String estado; // pendiente, aprobada, finalizada

    @Column(name = "ase_respuesta")
    private String respuestaProgramador;

    public Asesoria() {
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public String getSolicitanteNombre() {
        return solicitanteNombre;
    }

    public void setSolicitanteNombre(String solicitanteNombre) {
        this.solicitanteNombre = solicitanteNombre;
    }

    public Usuario getProgramador() {
        return programador;
    }

    public void setProgramador(Usuario programador) {
        this.programador = programador;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRespuestaProgramador() {
        return respuestaProgramador;
    }

    public void setRespuestaProgramador(String respuestaProgramador) {
        this.respuestaProgramador = respuestaProgramador;
    }
}
