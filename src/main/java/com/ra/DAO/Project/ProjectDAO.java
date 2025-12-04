package com.ra.DAO.Project;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ProjectDAO implements IProjectDAO {

    @Override
    public void create(Project project) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(project);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    /**
     * ❗❗ FIND ALL — KHÔNG FETCH 2 BAG
     * --> chỉ lấy Project, sau đó loadRelations()
     */
    @Override
    public List<Project> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT p FROM Project p ORDER BY p.id DESC",
                    Project.class
            ).list();
        }
    }

    /**
     * SEARCH dùng LIKE nhưng không fetch bag
     */
    @Override
    public List<Project> search(String keyword, int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT p FROM Project p WHERE p.name LIKE :kw ORDER BY p.id DESC",
                            Project.class
                    )
                    .setParameter("kw", "%" + keyword + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();
        }
    }

    /**
     * ❗❗ FIND BY ID — KHÔNG FETCH BAG
     */
    @Override
    public Project findFindById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Project.class, id);
        }
    }

    @Override
    public Optional<Project> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Project project = session.createQuery(
                            "FROM Project p WHERE p.name = :name",
                            Project.class
                    )
                    .setParameter("name", name)
                    .uniqueResult();

            return Optional.ofNullable(project);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * UPDATE — OK
     */
    @Override
    public void update(Project project) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            session.merge(project);

            // delete old relations
            session.createNativeQuery("DELETE FROM department_project WHERE projectId = :id")
                    .setParameter("id", project.getId())
                    .executeUpdate();

            session.createNativeQuery("DELETE FROM task_project WHERE projectId = :id")
                    .setParameter("id", project.getId())
                    .executeUpdate();

            // insert new relations
            for (Department d : project.getDepartments()) {
                session.createNativeQuery(
                                "INSERT INTO department_project (projectId, departmentId) VALUES (:pid, :did)")
                        .setParameter("pid", project.getId())
                        .setParameter("did", d.getId())
                        .executeUpdate();
            }

            for (Tasks t : project.getTasks()) {
                session.createNativeQuery(
                                "INSERT INTO task_project (projectId, taskId) VALUES (:pid, :tid)")
                        .setParameter("pid", project.getId())
                        .setParameter("tid", t.getId())
                        .executeUpdate();
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    /**
     * DELETE
     */
    @Override
    public boolean deleteFindById(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Project p = session.get(Project.class, id);
            if (p == null) return false;

            tx = session.beginTransaction();
            session.remove(p);
            tx.commit();

            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * LOAD RELATIONS — đúng chuẩn ManyToMany
     */
    public void loadRelations(Project p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // departments
            List<Department> deps = session.createQuery(
                    "SELECT d FROM Department d JOIN d.projects pj WHERE pj.id = :id",
                    Department.class
            ).setParameter("id", p.getId()).list();

            // tasks
            List<Tasks> tasks = session.createQuery(
                    "SELECT t FROM Tasks t JOIN t.projects pj WHERE pj.id = :id",
                    Tasks.class
            ).setParameter("id", p.getId()).list();

            p.setDepartments(deps);
            p.setTasks(tasks);
        }
    }
}
