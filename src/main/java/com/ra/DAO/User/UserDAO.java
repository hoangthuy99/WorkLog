package com.ra.DAO.User;

import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserDAO implements IUserDAO {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UserDAO.class.getName());

    @Override
    public Users create(Users user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            System.out.println("User created successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw e;
        }
        return user;
    }

    @Override
    public Users update(Users user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            System.out.println("User updated successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return user;
    }
    @Override
    public long countAll(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT COUNT(u) FROM Users u " +
                    "WHERE (u.userName LIKE :kw " +
                    "OR u.fullName LIKE :kw " +
                    "OR u.userCode LIKE :kw " +
                    "OR u.email LIKE :kw) " +
                    "AND u.deletedAt IS NULL";

            return session.createQuery(hql, Long.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .uniqueResult();
        }
    }





    @Override
    public boolean deleteFindById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Users user = session.get(Users.class, id);
            if (user != null) {
                user.setDeletedAt(LocalDateTime.now());  // 🔹 Đánh dấu xóa mềm
                session.update(user);                    // 🔹 Cập nhật thay vì delete
                transaction.commit();
                System.out.println("User soft-deleted (deletedAt set)!");
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }




    @Override
    public List<Users> findAll(String keyword, int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql =
                    "SELECT DISTINCT u FROM Users u " +
                            "LEFT JOIN FETCH u.department d " +
                            "LEFT JOIN FETCH u.role r " +
                            "LEFT JOIN FETCH u.tasks t " +
                            "WHERE (u.userName LIKE :kw " +
                            "OR u.fullName LIKE :kw " +
                            "OR u.userCode LIKE :kw " +
                            "OR u.email LIKE :kw) " +
                            "AND u.deletedAt IS NULL";

            return session.createQuery(hql, Users.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    @Override
    public List<Users> findAll() {
        logger.info("Finding all users");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT u FROM Users u " +
                    "LEFT JOIN FETCH u.tasks " +
                    "LEFT JOIN FETCH u.department " +
                    "LEFT JOIN FETCH u.role " +
                    "WHERE u.deletedAt IS NULL";
            return session.createQuery(hql, Users.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    @Override
    public Users findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT DISTINCT u FROM Users u " +
                    "LEFT JOIN FETCH u.tasks t " +
                    "LEFT JOIN FETCH u.department d " +
                    "LEFT JOIN FETCH u.role r " +
                    "WHERE u.id = :id AND u.deletedAt IS NULL";

            return session.createQuery(hql, Users.class)
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Users> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT u FROM Users u " +
                    "LEFT JOIN FETCH u.role r " +
                    "LEFT JOIN FETCH r.permissions " +
                    "LEFT JOIN FETCH u.department " +
                    "WHERE u.userName = :username AND u.deletedAt IS NULL";

            Users user = session.createQuery(hql, Users.class)
                    .setParameter("username", username)
                    .uniqueResult();

            return Optional.ofNullable(user);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Users> findUserByDepartmentId(int departmentId) {
        //TODO : Tìm kiếm user theo departmentId
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT u FROM Users u " +
                    "LEFT JOIN FETCH u.department d " +
                    "LEFT JOIN FETCH u.role " +
                    "WHERE d.id = :deptId AND u.deletedAt IS NULL " +
                    "ORDER BY u.userName";

            List<Users> users = session.createQuery(hql, Users.class)
                    .setParameter("deptId", departmentId)
                    .list();

            return users != null ? users : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
