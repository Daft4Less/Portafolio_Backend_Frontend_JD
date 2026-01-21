package ec.edu.ups.ppw.portafolio.business;

import ec.edu.ups.ppw.portafolio.dao.AsesoriaDAO;
import ec.edu.ups.ppw.portafolio.dao.HorarioDAO;
import ec.edu.ups.ppw.portafolio.dao.ProyectoDAO;
import ec.edu.ups.ppw.portafolio.dao.UsuarioDAO;
import ec.edu.ups.ppw.portafolio.model.Asesoria;
import ec.edu.ups.ppw.portafolio.model.Contacto;
import ec.edu.ups.ppw.portafolio.model.Horario;
import ec.edu.ups.ppw.portafolio.model.Proyecto;
import ec.edu.ups.ppw.portafolio.model.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Singleton
@Startup
public class Demo {

    @Inject
    private UsuarioDAO daoUsuario;

    @Inject
    private ProyectoDAO daoProyecto;

    @Inject
    private HorarioDAO daoHorario;

    @Inject
    private AsesoriaDAO daoAsesoria;

    @PostConstruct
    public void init() {
        System.out.println("--- INICIALIZANDO DATOS DE PRUEBA DEL PORTAFOLIO ---");
        
            // Crear Programador
            Usuario dev = new Usuario();
            dev.setUid("firebase_uid_12345");
            dev.setEmail("daniela@mail.com");
            dev.setDisplayName("Daniela Dev");
            dev.setPhotoURL("https://api.dicebear.com/7.x/avataaars/svg?seed=Daniela");
            dev.setRole("Programador");
            dev.setEspecialidad("Fullstack Java & Angular");
            dev.setDescripcion("Desarrolladora apasionada por la arquitectura de software.");

            Contacto cont = new Contacto();
            cont.setGithub("https://github.com/daniela");
            cont.setLinkedin("https://linkedin.com/in/daniela");
            cont.setWebsite("https://miprofolio.com");
            dev.setContacto(cont);

            daoUsuario.insert(dev);

            // Crear Usuario Normal
            Usuario user = new Usuario();
            user.setUid("user_abc");
            user.setEmail("juan@mail.com");
            user.setDisplayName("Juan Cliente");
            user.setRole("Usuario normal");
            daoUsuario.insert(user);

            // Crear Proyectos para el Programador
            Proyecto p1 = new Proyecto();
            p1.setId("proj_001");
            p1.setName("E-commerce App");
            p1.setDescription("Aplicación de ventas completa con pasarela de pagos.");
            p1.setParticipationType("Fullstack");
            p1.setTechnologies(Arrays.asList("Angular", "Jakarta EE", "PostgreSQL"));
            p1.setSection("Proyectos Laborales");
            p1.setUsuario(dev);
            daoProyecto.insert(p1);

            Proyecto p2 = new Proyecto();
            p2.setId("proj_002");
            p2.setName("App de Clima");
            p2.setDescription("Consulta de clima en tiempo real usando APIs externas.");
            p2.setParticipationType("Frontend");
            p2.setTechnologies(Arrays.asList("Angular", "TypeScript", "CSS"));
            p2.setSection("Proyectos Académicos");
            p2.setUsuario(dev);
            daoProyecto.insert(p2);

            // Crear el horario para el Programador
            Horario h1 = new Horario();
            h1.setId("sch_101");
            h1.setDayOfWeek(1); // Lunes
            h1.setStartTime("09:00");
            h1.setEndTime("12:00");
            h1.setAvailable(true);
            h1.setUsuario(dev);
            daoHorario.insert(h1);

            // Crear una Asesoría de prueba 
            Asesoria ase = new Asesoria();
            ase.setId("ase_999");
            ase.setSolicitante(user);
            ase.setSolicitanteNombre(user.getDisplayName());
            ase.setProgramador(dev);
            ase.setFecha(LocalDateTime.now().plusDays(2));
            ase.setComentario("Necesito ayuda con el mapeo de JPA.");
            ase.setEstado("pendiente");
            daoAsesoria.insert(ase);

            System.out.println("--- DATOS CARGADOS EXITOSAMENTE ---");
            
            // Verificación
            System.out.println("Usuarios registrados: " + daoUsuario.getAll().size());
            System.out.println("Proyectos registrados: " + daoProyecto.getAll().size());
            System.out.println("Asesorías activas: " + daoAsesoria.getAll().size());

    }
}
