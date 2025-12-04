package com.ra.DAO.Task;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Tasks;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TaskDAO implements ITaskDAO {

    @Override
    public void create(Tasks task) {
        Transaction tx = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();
            session.save(task);
            tx.commit();

        } catch (Exception e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Tasks task) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            // 1. Lấy dữ liệu gốc từ DB
            Tasks original = session.get(Tasks.class, task.getId());

            // 2. Xóa toàn bộ quan hệ cũ
            original.getDepartments().clear();
            original.getProjects().clear();

            session.flush(); // bắt buộc — giúp ORM xóa ngay ở bảng trung gian

            // 3. Gán dữ liệu mới
            original.setName(task.getName());
            original.setDepartments(task.getDepartments());
            original.setProjects(task.getProjects());

            // 4. Lưu lại
            session.merge(original);

            tx.commit();
            System.out.println("Task updated successfully!");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }


    @Override
    public boolean deleteFindById(int id) {
        Transaction tx = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            Tasks task = session.get(Tasks.class, id);
            if(task == null) return false;

            tx = session.beginTransaction();
            session.delete(task);
            tx.commit();
            return true;

        } catch (Exception e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Tasks> findFindById(int id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            Tasks task = session.createQuery(
                    "SELECT t FROM Tasks t " +
                            "LEFT JOIN FETCH t.projects " +
                            "LEFT JOIN FETCH t.departments " +
                            "LEFT JOIN FETCH t.users " +
                            "WHERE t.id = :id",
                    Tasks.class
            ).setParameter("id", id).uniqueResult();

            return Optional.ofNullable(task);
        }
    }

    @Override
    public List<Tasks> search(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Tasks> tasks = session.createQuery(
                            "SELECT t FROM Tasks t WHERE t.name LIKE :kw OR t.taskCode LIKE :kw",
                            Tasks.class
                    )
                    .setParameter("kw", "%" + keyword + "%")
                    .list();

            // load thủ công tránh MultipleBagFetchException
            for (Tasks t : tasks) {
                Hibernate.initialize(t.getDepartments());
                Hibernate.initialize(t.getProjects());
                Hibernate.initialize(t.getUsers());
            }

            return tasks;
        }
    }


    @Override
    public List<Tasks> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Tasks> tasks = session.createQuery("FROM Tasks", Tasks.class).list();

            // Tránh LazyInitializationException
            for(Tasks t : tasks) {
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

            if (task != null) {
                Hibernate.initialize(task.getProjects());
                Hibernate.initialize(task.getDepartments());
                Hibernate.initialize(task.getUsers());
            }

            return Optional.ofNullable(task);
        }
    }


    /**
     * Lấy tất cả task thuộc danh sách department
     */
    public List<Tasks> findTasksByDepartments(List<Department> departments) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Integer> depIds = departments.stream()
                    .map(Department::getId)
                    .toList();

            return session.createQuery(
                            "SELECT DISTINCT t FROM Tasks t " +
                                    "JOIN t.departments d " +
                                    "WHERE d.id IN (:ids)",
                            Tasks.class
                    ).setParameter("ids", depIds)
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
