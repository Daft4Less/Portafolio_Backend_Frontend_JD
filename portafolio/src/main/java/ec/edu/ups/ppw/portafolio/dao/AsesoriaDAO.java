package ec.edu.ups.ppw.portafolio.dao;

import ec.edu.ups.ppw.portafolio.model.Asesoria;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime; // Importar LocalDateTime
import java.util.List;

@Stateless
public class AsesoriaDAO {

    @PersistenceContext
    private EntityManager em;

    public void insert(Asesoria asesoria) {
        em.persist(asesoria);
    }

    public void update(Asesoria asesoria) {
        em.merge(asesoria);
    }

    public Asesoria read(String id) {
        return em.find(Asesoria.class, id);
    }

    public void delete(String id) {
        Asesoria a = em.find(Asesoria.class, id);
        if (a != null) {
            em.remove(a);
        }
    }

    public List<Asesoria> getAll() {
        String jpql = "SELECT a FROM Asesoria a";
        TypedQuery<Asesoria> q = em.createQuery(jpql, Asesoria.class);
        return q.getResultList();
    }
    
    // Asesorías que pide un usuario
    public List<Asesoria> getBySolicitante(String solicitanteUid) {
        String jpql = "SELECT a FROM Asesoria a WHERE a.solicitante.uid = :uid";
        TypedQuery<Asesoria> q = em.createQuery(jpql, Asesoria.class);
        q.setParameter("uid", solicitanteUid);
        return q.getResultList();
    }

    // Asesorías del programador 
    public List<Asesoria> getByProgramador(String programadorUid) {
        String jpql = "SELECT a FROM Asesoria a WHERE a.programador.uid = :uid";
        TypedQuery<Asesoria> q = em.createQuery(jpql, Asesoria.class);
        q.setParameter("uid", programadorUid);
        return q.getResultList();
    }
    
    // Nuevo método para el reporte de asesorías por programador, fecha y estado
    public List<Asesoria> findAsesoriasByCriteria(String programadorUid, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado) {
        StringBuilder jpql = new StringBuilder("SELECT a FROM Asesoria a WHERE 1=1");

        if (programadorUid != null && !programadorUid.isEmpty()) {
            jpql.append(" AND a.programador.uid = :programadorUid");
        }
        if (fechaInicio != null) {
            jpql.append(" AND a.fecha >= :fechaInicio");
        }
        if (fechaFin != null) {
            jpql.append(" AND a.fecha <= :fechaFin");
        }
        if (estado != null && !estado.isEmpty()) {
            jpql.append(" AND a.estado = :estado");
        }

        TypedQuery<Asesoria> query = em.createQuery(jpql.toString(), Asesoria.class);

        if (programadorUid != null && !programadorUid.isEmpty()) {
            query.setParameter("programadorUid", programadorUid);
        }
        if (fechaInicio != null) {
            query.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            query.setParameter("fechaFin", fechaFin);
        }
        if (estado != null && !estado.isEmpty()) {
            query.setParameter("estado", estado);
        }

        return query.getResultList();
    }
}
