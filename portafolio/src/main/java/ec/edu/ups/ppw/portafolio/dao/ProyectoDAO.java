package ec.edu.ups.ppw.portafolio.dao;

import ec.edu.ups.ppw.portafolio.model.Proyecto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ProyectoDAO {

    @PersistenceContext
    private EntityManager em;

    public void insert(Proyecto proyecto) {
        em.persist(proyecto);
    }

    public void update(Proyecto proyecto) {
        em.merge(proyecto);
    }

    public Proyecto read(String id) {
        return em.find(Proyecto.class, id);
    }

    public void delete(String id) {
        Proyecto p = em.find(Proyecto.class, id);
        if (p != null) {
            em.remove(p);
        }
    }

    public List<Proyecto> getAll() {
        String jpql = "SELECT p FROM Proyecto p";
        TypedQuery<Proyecto> q = em.createQuery(jpql, Proyecto.class);
        return q.getResultList();
    }
    
    // Buscar proyectos de un usuario especifico
    public List<Proyecto> getByUsuario(String userUid) {
        String jpql = "SELECT p FROM Proyecto p WHERE p.usuario.uid = :uid";
        TypedQuery<Proyecto> q = em.createQuery(jpql, Proyecto.class);
        q.setParameter("uid", userUid);
        return q.getResultList();
    }
}
