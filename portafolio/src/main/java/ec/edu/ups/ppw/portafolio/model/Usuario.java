package ec.edu.ups.ppw.portafolio.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_USUARIO")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "usu_uid")
    private String uid;

    @Column(name = "usu_email", nullable = false)
    private String email;

    @Column(name = "usu_display_name", nullable = false)
    private String displayName;

    @Column(name = "usu_photo_url")
    private String photoURL;

    @Column(name = "usu_role", nullable = false)
    private String role; // Administrador, Programador, Usuario normal

    @Column(name = "usu_especialidad")
    private String especialidad;

    @Column(name = "usu_descripcion")
    private String descripcion;

    @Embedded
    private Contacto contacto;

    // Relaciones
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Proyecto> projects;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Horario> schedules;

    public Usuario() {
        this.contacto = new Contacto();
        this.projects = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    // Getters y Setters

    @JsonbTransient
    public List<Proyecto> getProjects() {
        return projects;
    }

    public void setProjects(List<Proyecto> projects) {
        this.projects = projects;
    }

    @JsonbTransient
    public List<Horario> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Horario> schedules) {
        this.schedules = schedules;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonbTransient
    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }
}
