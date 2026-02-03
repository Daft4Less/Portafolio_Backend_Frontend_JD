package ec.edu.ups.ppw.portafolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@Table(name = "TBL_HORARIO")
public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "hor_id")
    private String id;

    @Column(name = "hor_day_of_week", nullable = false)
    private int dayOfWeek; // 0=Domingo, 1=Lunes

    @Column(name = "hor_start_time", nullable = false)
    private String startTime; // "HH:mm"

    @Column(name = "hor_end_time", nullable = false)
    private String endTime; // "HH:mm"

    @Column(name = "hor_is_available", nullable = false)
    private boolean isAvailable;

    @Column(name = "hor_start_date_off")
    private LocalDate startDateOffService; 

    @Column(name = "hor_end_date_off")
    private LocalDate endDateOffService;

    // Relación con Usuario (Programador)
    @ManyToOne
    @JoinColumn(name = "usu_uid")
    @JsonbTransient
    private Usuario usuario;

    public Horario() {
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public LocalDate getStartDateOffService() {
        return startDateOffService;
    }

    public void setStartDateOffService(LocalDate startDateOffService) {
        this.startDateOffService = startDateOffService;
    }

    public LocalDate getEndDateOffService() {
        return endDateOffService;
    }

    public void setEndDateOffService(LocalDate endDateOffService) {
        this.endDateOffService = endDateOffService;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
