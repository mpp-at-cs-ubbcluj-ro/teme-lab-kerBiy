package persistence.hibernate;

import models.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.interfaces.IEmployeeRepository;

import java.util.Optional;

public class EmployeeRepositoryHibernate implements IEmployeeRepository {

    @Override
    public Optional<Employee> findBy(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employee emp = session.createQuery(
                            "FROM Employee WHERE username = :u AND password = :p", Employee.class)
                    .setParameter("u", username)
                    .setParameter("p", password)
                    .uniqueResult();

            return Optional.ofNullable(emp);
        }
    }

    @Override
    public Optional<Employee> findOne(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Employee.class, id));
        }
    }

    @Override
    public Iterable<Employee> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee", Employee.class).list();
        }
    }

    @Override
    public Optional<Employee> save(Employee entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<Employee> delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) {
                session.remove(emp);
            }
            tx.commit();
            return Optional.ofNullable(emp);
        }
    }

    @Override
    public Optional<Employee> update(Employee entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return Optional.ofNullable(entity);
        }
    }
}