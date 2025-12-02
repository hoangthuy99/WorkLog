package com.ra.DAO.Department;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class DepartmentDAO implements IDepartmentDAO {

    @Override
    public void create(Department department) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            // Save department
            session.save(department);

            // Insert department_project
            if (department.getProjects() != null) {
                for (Project p : department.getProjects()) {
                    session.createNativeQuery(
                                    "INSERT INTO department_project (departmentId, projectId) VALUES (:d, :p)")
                            .setParameter("d", department.getId())
                            .setParameter("p", p.getId())
                            .executeUpdate();
                }
            }

            // Insert department_task
            if (department.getTasks() != null) {
                for (Tasks t : department.getTasks()) {
                    session.createNativeQuery(
                                    "INSERT INTO department_task (departmentId, taskId) VALUES (:d, :t)")
                            .setParameter("d", department.getId())
                            .setParameter("t", t.getId())
                            .executeUpdate();
                }
            }

            tx.commit();
            System.out.println("Department created successfully!");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }


    @Override
    public void update(Department department) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(department);

            session.createNativeQuery("DELETE FROM department_project WHERE departmentId = :id")
                    .setParameter("id", department.getId())
                    .executeUpdate();

            session.createNativeQuery("DELETE FROM department_task WHERE departmentId = :id")
                    .setParameter("id", department.getId())
                    .executeUpdate();

            for (Project p : department.getProjects()) {
                session.createNativeQuery(
                                "INSERT INTO department_project (departmentId, projectId) VALUES (:d, :p)")
                        .setParameter("d", department.getId())
                        .setParameter("p", p.getId())
                        .executeUpdate();
            }

            for (Tasks t : department.getTasks()) {
                session.createNativeQuery(
                                "INSERT INTO department_task (departmentId, taskId) VALUES (:d, :t)")
                        .setParameter("d", department.getId())
                        .setParameter("t", t.getId())
                        .executeUpdate();
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteFindById(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Department dep = session.get(Department.class, id);
            if (dep == null) return false;

            tx = session.beginTransaction();

            session.createNativeQuery("DELETE FROM department_project WHERE departmentId = :id")
                    .setParameter("id", id).executeUpdate();

            session.createNativeQuery("DELETE FROM department_task WHERE departmentId = :id")
                    .setParameter("id", id).executeUpdate();

            session.remove(dep);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Department> search(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Department d WHERE d.name LIKE :kw",
                            Department.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .list();
        }
    }

    @Override
    public Optional<Department> findFindById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Department dep = session.get(Department.class, id);
            return Optional.ofNullable(dep);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Override
    public List<Department> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Department> list = session.createQuery("FROM Department", Department.class).list();

            for (Department d : list) {
                Hibernate.initialize(d.getProjects());
                Hibernate.initialize(d.getTasks());
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<Department> findFindByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Department dept = session.createQuery(
                            "FROM Department d WHERE d.name = :name",
                            Department.class)
                    .setParameter("name", name)
                    .uniqueResult();

            return Optional.ofNullable(dept);
        }
    }
}
