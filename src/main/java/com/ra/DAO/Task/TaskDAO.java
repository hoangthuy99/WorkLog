package com.ra.DAO.Task;

import com.ra.Model.Entity.Tasks;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TaskDAO implements ITaskDAO {

    @Override
    public void create(Tasks task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(task);
            transaction.commit();
            System.out.println("Task created successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Tasks task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(task);
            transaction.commit();
            System.out.println("Task updated successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteFindById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Tasks task = session.get(Tasks.class, id);
            if (task == null) return false;

            transaction = session.beginTransaction();
            session.delete(task);
            transaction.commit();

            return true;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Tasks> findFindById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Tasks task = session.createQuery(
                            "SELECT t FROM Tasks t " +
                                    "LEFT JOIN FETCH t.projects " +
                                    "LEFT JOIN FETCH t.departments " +
                                    "LEFT JOIN FETCH t.users " +
                                    "WHERE t.id = :id", Tasks.class)
                    .setParameter("id", id)
                    .uniqueResult();

            return Optional.ofNullable(task);
        }
    }

    @Override
    public List<Tasks> search(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "SELECT DISTINCT t FROM Tasks t " +
                                    "LEFT JOIN FETCH t.projects " +
                                    "LEFT JOIN FETCH t.departments " +
                                    "WHERE t.name LIKE :kw OR t.taskCode LIKE :kw",
                            Tasks.class
                    )
                    .setParameter("kw", "%" + keyword + "%")
                    .list();
        }
    }

    @Override
    public List<Tasks> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // 1 — Lấy danh sách Task thông thường, KHÔNG JOIN FETCH
            List<Tasks> tasks = session.createQuery(
                    "FROM Tasks", Tasks.class
            ).list();

            // 2 — Tải thủ công các collection để tránh LazyInitializationException
            for (Tasks t : tasks) {
                Hibernate.initialize(t.getProjects());
                Hibernate.initialize(t.getDepartments());
                Hibernate.initialize(t.getUsers());
            }

            return tasks;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    @Override
    public Optional<Tasks> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Tasks task = session.createQuery(
                            "FROM Tasks t WHERE t.name = :name",
                            Tasks.class
                    )
                    .setParameter("name", name)
                    .uniqueResult();

            return Optional.ofNullable(task);
        }
    }
}
