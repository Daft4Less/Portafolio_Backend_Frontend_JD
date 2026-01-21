package ec.edu.ups.ppw.portafolio.dao;

import ec.edu.ups.ppw.portafolio.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UsuarioDAO {

    @PersistenceContext
    private EntityManager em;

    public void insert(Usuario usuario) {
        em.persist(usuario);
    }

    public void update(Usuario usuario) {
        em.merge(usuario);
    }

    public Usuario read(String uid) {
        return em.find(Usuario.class, uid);
    }

    public void delete(String uid) {
        Usuario u = em.find(Usuario.class, uid);
        if (u != null) {
            em.remove(u);
        }
    }

    public List<Usuario> getAll() {
        String jpql = "SELECT u FROM Usuario u";
        TypedQuery<Usuario> q = em.createQuery(jpql, Usuario.class);
        return q.getResultList();
    }
    
    // Método: Buscar por Rol
    public List<Usuario> getByRole(String role) {
        String jpql = "SELECT u FROM Usuario u WHERE u.role = :role";
        TypedQuery<Usuario> q = em.createQuery(jpql, Usuario.class);
        q.setParameter("role", role);
        return q.getResultList();
    }
}
