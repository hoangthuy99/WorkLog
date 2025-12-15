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

            // CHECK duplicate name
            Long countName = session.createQuery(
                            "SELECT COUNT(d) FROM Department d WHERE d.name = :name AND d.deletedAt IS NULL",
                            Long.class
                    ).setParameter("name", department.getName())
                    .uniqueResult();

            if (countName != null && countName > 0) {
                throw new RuntimeException("部署名が既に存在しています。");
            }

            //  CHECK duplicate code
            Long countCode = session.createQuery(
                    "SELECT COUNT(d) FROM Department d WHERE d.departmentCode = :code AND d.deletedAt IS NULL",
                    Long.class
                    ).setParameter("code", department.getDepartmentCode())
                    .uniqueResult();

            if (countCode != null && countCode > 0) {
                throw new RuntimeException("部署コードが既に存在しています。");
            }

            //  SAVE
            tx = session.beginTransaction();
            session.save(department);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }


    @Override
    public void update(Department d) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // CHECK duplicate name (except itself)
            Long countName = session.createQuery(
                            "SELECT COUNT(d) FROM Department d WHERE d.name = :name AND d.id <> :id AND d.deletedAt IS NULL",
                            Long.class
                    )
                    .setParameter("name", d.getName())
                    .setParameter("id", d.getId())
                    .uniqueResult();

            if (countName != null && countName > 0) {
                throw new RuntimeException("部署名は既に使用されています。");
            }

// CHECK duplicate departmentCode (except itself)
            Long countCode = session.createQuery(
                            "SELECT COUNT(d) FROM Department d WHERE d.departmentCode = :code AND d.id <> :id AND d.deletedAt IS NULL",
                            Long.class
                    )
                    .setParameter("code", d.getDepartmentCode())
                    .setParameter("id", d.getId())
                    .uniqueResult();

            if (countCode != null && countCode > 0) {
                throw new RuntimeException("部署コードは既に使用されています。");
            }


            tx = session.beginTransaction();

            // ====== 1) Lấy bản ghi gốc từ DB ======
            Department dbDep = session.get(Department.class, d.getId());
            if (dbDep == null) return;

            // ====== 2) Update field đơn ======
            dbDep.setName(d.getName());

            // ============================================
            //  UPDATE TASK (Department là owner → OK)
            // ============================================
            dbDep.getTasks().clear();
            session.flush(); // tránh duplicate row

            if (d.getTasks() != null) {
                for (Tasks t : d.getTasks()) {
                    Tasks tRef = session.get(Tasks.class, t.getId());
                    dbDep.getTasks().add(tRef);
                }
            }

            // ============================================
            //  UPDATE PROJECT (Project là owner → phải update bên Project)
            // ============================================

            // 2.1 XÓA RELATION cũ
            for (Project oldP : dbDep.getProjects()) {
                oldP.getDepartments().remove(dbDep);
                session.update(oldP);
            }
            session.flush();

            dbDep.getProjects().clear();

            // 2.2 THÊM LIÊN KẾT MỚI
            if (d.getProjects() != null) {
                for (Project p : d.getProjects()) {
                    Project pRef = session.get(Project.class, p.getId());
                    pRef.getDepartments().add(dbDep);
                    dbDep.getProjects().add(pRef);
                    session.update(pRef); // owner update
                }
            }

            // ====== 3) Lưu vào DB ======
            session.update(dbDep);

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

            //  CHECK — Department có User?
            Long userCount = session.createQuery(
                    "SELECT COUNT(u) FROM Users u WHERE u.department.id = :id",
                    Long.class
            ).setParameter("id", id).uniqueResult();

            if (userCount != null && userCount > 0) {
                throw new RuntimeException("部署はユーザーに使用されているため、削除できません。");
            }

            // CHECK — Department có trong WorkRecord (qua Task)?
            Long workRecordCount = session.createQuery(
                    "SELECT COUNT(w) FROM WorkRecord w " +
                            "JOIN w.task t " +
                            "JOIN t.departments d " +
                            "WHERE d.id = :id",
                    Long.class
            ).setParameter("id", id).uniqueResult();

            if (workRecordCount != null && workRecordCount > 0) {
                throw new RuntimeException("部署は勤務記録で使用されているため、削除できません。");
            }

            // SOFT DELETE
            tx = session.beginTransaction();
            dep.setDeletedAt(java.time.LocalDateTime.now());
            session.update(dep);
            tx.commit();

            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public List<Department> search(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql =
                    "SELECT DISTINCT d FROM Department d " +
                            "LEFT JOIN FETCH d.projects " +
                            "LEFT JOIN FETCH d.tasks " +
                            "WHERE d.deletedAt IS NULL AND d.name LIKE :kw";

            return session.createQuery(hql, Department.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .list();
        }
    }


    @Override
    public Optional<Department> findFindById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Department dep = session.get(Department.class, id);
            return Optional.ofNullable(dep);
        }
    }

    @Override
    public Optional<Department> findFindByIdFetchAll(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Department dep = session.get(Department.class, id);

        dep.getProjects().size();
        dep.getTasks().size();

        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(dep);
    }

    @Override
    public List<Department> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Department> list = session.createQuery(
                    "FROM Department d WHERE d.deletedAt IS NULL",
                    Department.class
            ).list();


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
                String hql = "FROM Department d WHERE d.name = :name AND d.deletedAt IS NULL";
                Department dept = session.createQuery(hql, Department.class)
                        .setParameter("name", name)
                        .uniqueResult();
                return Optional.ofNullable(dept);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return Optional.empty();
            }


    }


}
