package com.ra.DAO.Project;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProjectDAO implements IProjectDAO {

    @Override
    public void create(Project project) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Long countName = session.createQuery(
                            "SELECT COUNT(p) FROM Project p WHERE p.name = :name AND p.deletedAt IS NULL",
                            Long.class
                    )
                    .setParameter("name", project.getName())
                    .uniqueResult();

            if (countName != null && countName > 0) {
                throw new RuntimeException("プロジェクト名が既に存在しています。");
            }

            Long countCode = session.createQuery(
                            "SELECT COUNT(p) FROM Project p WHERE p.projectCode = :code",
                            Long.class
                    )
                    .setParameter("code", project.getProjectCode())
                    .uniqueResult();

            if (countCode != null && countCode > 0) {
                throw new RuntimeException("プロジェクトコードが既に存在しています。");
            }

            tx = session.beginTransaction();
            session.persist(project);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }



    @Override
    public List<Project> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT p FROM Project p WHERE p.deletedAt IS NULL ORDER BY p.id DESC",
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
                            "SELECT p FROM Project p \n" +
                                    "WHERE p.deletedAt IS NULL \n" +
                                    "AND p.name LIKE :kw \n" +
                                    "ORDER BY p.id DESC\n",
                            Project.class
                    )
                    .setParameter("kw", "%" + keyword + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();
        }
    }


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


    @Override
    public boolean deleteFindById(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Long count = session.createQuery(
                    "SELECT COUNT(w) FROM WorkRecord w WHERE w.project.id = :pid",
                    Long.class
            ).setParameter("pid", id).uniqueResult();

            if (count != null && count > 0) {
                throw new RuntimeException("プロジェクトは勤務記録に使用されているため、削除できません。");
            }

            Project p = session.get(Project.class, id);
            if (p == null) return false;

            tx = session.beginTransaction();

            p.setDeletedAt(LocalDateTime.now());
            session.update(p);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }



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
