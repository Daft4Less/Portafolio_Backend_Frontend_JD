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
                try {
                    // Todos los datos de prueba han sido eliminados.
                    // Si necesitas datos de prueba, puedes agregarlos aquí.
                } catch (Exception e) {
                    System.err.println("Error al inicializar datos de prueba: " + e.getMessage());
                }
                System.out.println("--- INICIALIZACIÓN DE DATOS DE PRUEBA FINALIZADA ---");
    }}
