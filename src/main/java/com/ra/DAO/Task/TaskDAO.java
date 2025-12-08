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

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // 1️⃣ CHECK duplicate task name
            Long countName = session.createQuery(
                            "SELECT COUNT(t) FROM Tasks t WHERE t.name = :name AND t.deletedAt IS NULL",
                            Long.class
                    )
                    .setParameter("name", task.getName())
                    .uniqueResult();

            if (countName != null && countName > 0) {
                throw new RuntimeException("タスク名が既に存在しています。");
            }

            // 2️⃣ CHECK duplicate taskCode
            Long countCode = session.createQuery(
                            "SELECT COUNT(t) FROM Tasks t WHERE t.taskCode = :code",
                            Long.class
                    )
                    .setParameter("code", task.getTaskCode())
                    .uniqueResult();

            if (countCode != null && countCode > 0) {
                throw new RuntimeException("タスクコードが既に存在しています。");
            }

            // 3️⃣ Thực hiện INSERT
            tx = session.beginTransaction();
            session.save(task);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;  // 🚀 đẩy lỗi lên Controller để UI nhận và hiển thị
        }
    }


    @Override
    public void update(Tasks task) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // 0️⃣ CHECK duplicate name (trừ chính nó)
            Long countName = session.createQuery(
                            "SELECT COUNT(t) FROM Tasks t WHERE t.name = :name AND t.id <> :id AND t.deletedAt IS NULL",
                            Long.class
                    )
                    .setParameter("name", task.getName())
                    .setParameter("id", task.getId())
                    .uniqueResult();

            if (countName != null && countName > 0) {
                throw new RuntimeException("タスク名は既に使用されています。");
            }

            // 1️⃣ CHECK duplicate taskCode
            Long countCode = session.createQuery(
                            "SELECT COUNT(t) FROM Tasks t WHERE t.taskCode = :code AND t.id <> :id AND t.deletedAt IS NULL",
                            Long.class
                    )
                    .setParameter("code", task.getTaskCode())
                    .setParameter("id", task.getId())
                    .uniqueResult();

            if (countCode != null && countCode > 0) {
                throw new RuntimeException("タスクコードは既に使用されています。");
            }

            // ========== TIẾP TỤC PHẦN UPDATE CŨ ==========
            tx = session.beginTransaction();

            Tasks original = session.get(Tasks.class, task.getId());

            original.getDepartments().clear();
            original.getProjects().clear();
            session.flush();

            original.setName(task.getName());
            original.setTaskCode(task.getTaskCode()); // ❗ bạn nên update cả taskCode
            original.setDepartments(task.getDepartments());
            original.setProjects(task.getProjects());

            session.merge(original);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }


    @Override
    public boolean deleteFindById(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // 1️⃣ Check xem Task có đang được sử dụng trong WorkRecord không
            Long count = session.createQuery(
                    "SELECT COUNT(w) FROM WorkRecord w WHERE w.task.id = :tid",
                    Long.class
            ).setParameter("tid", id).uniqueResult();

            if (count != null && count > 0) {
                throw new RuntimeException("タスクは勤務記録に使用されているため、削除できません。");
            }

            // 2️⃣ Lấy Task từ DB
            Tasks task = session.get(Tasks.class, id);
            if (task == null) return false;

            tx = session.beginTransaction();

            // 3️⃣ Soft Delete (KHÔNG XÓA KHỎI DB)
            task.setDeletedAt(java.time.LocalDateTime.now());
            session.update(task);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // QUAN TRỌNG: ném lỗi lên UI
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
                            "SELECT t FROM Tasks t WHERE t.deletedAt IS NULL \n" +
                                    " AND (t.name LIKE :kw OR t.taskCode LIKE :kw)",
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

            List<Tasks> tasks = session.createQuery(
                    "FROM Tasks t WHERE t.deletedAt IS NULL",
                    Tasks.class
            ).list();


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
                            "FROM Tasks t WHERE t.name = :name AND t.deletedAt IS NULL",
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

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
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
