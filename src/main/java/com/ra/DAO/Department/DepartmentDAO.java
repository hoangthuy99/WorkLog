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
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(department);
            transaction.commit();
            System.out.println("Department created successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Department d) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            // ====== 1) Lấy bản ghi gốc từ DB ======
            Department dbDep = session.get(Department.class, d.getId());
            if (dbDep == null) return;

            // ====== 2) Update field đơn ======
            dbDep.setName(d.getName());

            // ============================================
            // 🔥 UPDATE TASK (Department là owner → OK)
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
            // 🔥 UPDATE PROJECT (Project là owner → phải update bên Project)
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
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Department dep = session.get(Department.class, id);
            if (dep == null) return false;

            transaction = session.beginTransaction();
            session.remove(dep);
            transaction.commit();

            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
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
                String hql = "FROM Department d WHERE d.name = :name";
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
