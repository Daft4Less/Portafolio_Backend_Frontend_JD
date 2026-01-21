package ec.edu.ups.ppw.portafolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Contacto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "cont_github")
    private String github;

    @Column(name = "cont_linkedin")
    private String linkedin;

    @Column(name = "cont_website")
    private String website;

    // Constructores
    public Contacto() {
    }

    public Contacto(String github, String linkedin, String website) {
        this.github = github;
        this.linkedin = linkedin;
        this.website = website;
    }

    // Getters y Setters
    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Contacto [github=" + github + ", linkedin=" + linkedin + ", website=" + website + "]";
    }
}
