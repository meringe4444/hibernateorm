package ni.edu.uam.hibernateorm.repository;

import ni.edu.uam.hibernateorm.config.HibernateUtil;
import ni.edu.uam.hibernateorm.model.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositoryHibernate implements UsuarioRepository {

    @Override
    public List<Usuario> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Usuario ORDER BY nombre ASC", Usuario.class).list();
        }
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        }
    }

    @Override
    public void save(Usuario usuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            if (usuario.getId() == null) {
                usuario.setId(UUID.randomUUID());
            }
            session.persist(usuario);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public void update(Usuario usuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(usuario);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public void delete(UUID id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Usuario usuario = session.find(Usuario.class, id);
            if (usuario != null) {
                session.remove(usuario);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public Optional<Usuario> findByLogin(String usuario, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Usuario WHERE usuario = :user AND password = :pass",
                            Usuario.class)
                    .setParameter("user", usuario)
                    .setParameter("pass", password)
                    .uniqueResultOptional();
        }
    }
}
