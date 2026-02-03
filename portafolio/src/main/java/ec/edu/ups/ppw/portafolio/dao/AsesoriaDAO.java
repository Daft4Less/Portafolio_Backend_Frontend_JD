package ec.edu.ups.ppw.portafolio.dao;

import ec.edu.ups.ppw.portafolio.model.Asesoria;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
}
