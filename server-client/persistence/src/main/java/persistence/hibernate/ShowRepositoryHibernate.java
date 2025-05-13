package persistence.hibernate;

import models.Show;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.interfaces.IShowRepository;

import java.util.List;
import java.util.Optional;

public class ShowRepositoryHibernate implements IShowRepository {

    @Override
    public Optional<Show> findOne(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Show.class, id));
        }
    }

    @Override
    public Iterable<Show> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Show", Show.class).list();
        }
    }

    @Override
    public Optional<Show> save(Show entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Show> delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Show show = session.get(Show.class, id);
            if (show != null) {
                session.remove(show);
                tx.commit();
                return Optional.of(show);
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Show> update(Show entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return Optional.of(entity);
        }
    }
}