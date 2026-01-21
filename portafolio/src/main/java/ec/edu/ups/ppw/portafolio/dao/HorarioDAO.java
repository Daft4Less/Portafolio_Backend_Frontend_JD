package ec.edu.ups.ppw.portafolio.dao;

import ec.edu.ups.ppw.portafolio.model.Horario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class HorarioDAO {

    @PersistenceContext
    private EntityManager em;

    public void insert(Horario horario) {
        em.persist(horario);
    }

    public void update(Horario horario) {
        em.merge(horario);
    }

    public Horario read(String id) {
        return em.find(Horario.class, id);
    }

    public void delete(String id) {
        Horario h = em.find(Horario.class, id);
        if (h != null) {
            em.remove(h);
        }
    }

    public List<Horario> getAll() {
        String jpql = "SELECT h FROM Horario h";
        TypedQuery<Horario> q = em.createQuery(jpql, Horario.class);
        return q.getResultList();
    }

    // Obtener horarios de un programador
    public List<Horario> getByProgramador(String programadorUid) {
        String jpql = "SELECT h FROM Horario h WHERE h.usuario.uid = :uid ORDER BY h.dayOfWeek ASC, h.startTime ASC";
        TypedQuery<Horario> q = em.createQuery(jpql, Horario.class);
        q.setParameter("uid", programadorUid);
        return q.getResultList();
    }
}
