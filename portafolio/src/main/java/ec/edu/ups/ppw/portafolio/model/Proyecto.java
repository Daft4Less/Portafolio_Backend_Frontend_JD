package ec.edu.ups.ppw.portafolio.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@Table(name = "TBL_PROYECTO")
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pro_id")
    private String id;

    @Column(name = "pro_name", nullable = false)
    private String name;

    @Lob // Para textos largos (Large Object)
    @Column(name = "pro_description", nullable = false)
    private String description;

    @Column(name = "pro_participation_type", nullable = false)
    private String participationType;

    // Lista de tecnologias 
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TBL_PROYECTO_TECNOLOGIAS", joinColumns = @JoinColumn(name = "pro_id"))
    @Column(name = "tec_nombre")
    private List<String> technologies;

    @Column(name = "pro_repo_link")
    private String repositoryLink;

    @Column(name = "pro_deploy_link")
    private String deploymentLink;

    @Column(name = "pro_section", nullable = false)
    private String section;

    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "usu_uid", nullable = false) // FK
    @JsonbTransient
    private Usuario usuario;

    public Proyecto() {
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParticipationType() {
        return participationType;
    }

    public void setParticipationType(String participationType) {
        this.participationType = participationType;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public String getRepositoryLink() {
        return repositoryLink;
    }

    public void setRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }

    public String getDeploymentLink() {
        return deploymentLink;
    }

    public void setDeploymentLink(String deploymentLink) {
        this.deploymentLink = deploymentLink;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
