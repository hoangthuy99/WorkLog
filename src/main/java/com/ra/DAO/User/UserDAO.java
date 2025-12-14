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

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(UserDAO.class.getName());

    @Override
    public Users create(Users user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // 🔹 normalize username
            String username = user.getUserName() == null ? "" : user.getUserName().trim();
            user.setUserName(username);

            // ✅ check trùng username (chỉ user chưa bị xóa mềm)
            Long count = session.createQuery(
                            "SELECT COUNT(u.id) FROM Users u " +
                                    "WHERE u.userName = :username AND u.deletedAt IS NULL",
                            Long.class
                    )
                    .setParameter("username", username)
                    .uniqueResult();

            if (count != null && count > 0) {
                throw new IllegalArgumentException(
                        "ユーザー名「" + username + "」は既に使用されています。"
                );
            }

            session.save(user);
            transaction.commit();
            logger.info("User created successfully!");
            return user;

        } catch (IllegalArgumentException e) {
            if (transaction != null) transaction.rollback();
            throw e;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("ユーザーの作成中にエラーが発生しました。");
        }
    }

    @Override
    public Users update(Users user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // 🔹 normalize username
            String username = user.getUserName() == null ? "" : user.getUserName().trim();
            user.setUserName(username);

            // ✅ check trùng username (trừ chính user đang edit)
            Long count = session.createQuery(
                            "SELECT COUNT(u.id) FROM Users u " +
                                    "WHERE u.userName = :username " +
                                    "AND u.deletedAt IS NULL " +
                                    "AND u.id <> :id",
                            Long.class
                    )
                    .setParameter("username", username)
                    .setParameter("id", user.getId())
                    .uniqueResult();

            if (count != null && count > 0) {
                throw new IllegalArgumentException(
                        "ユーザー名「" + username + "」は既に使用されています。"
                );
            }

            session.update(user);
            transaction.commit();
            logger.info("User updated successfully!");
            return user;

        } catch (IllegalArgumentException e) {
            if (transaction != null) transaction.rollback();
            throw e;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException("ユーザー更新中にエラーが発生しました。");
        }
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
                user.setDeletedAt(LocalDateTime.now());
                session.update(user);
                transaction.commit();
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT u FROM Users u " +
                    "LEFT JOIN FETCH u.tasks " +
                    "LEFT JOIN FETCH u.department " +
                    "LEFT JOIN FETCH u.role " +
                    "WHERE u.deletedAt IS NULL";
            return session.createQuery(hql, Users.class).getResultList();
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
                    "WHERE u.userName = :username AND u.deletedAt IS NULL " +
                    "ORDER BY u.id ASC";

            List<Users> list = session.createQuery(hql, Users.class)
                    .setParameter("username", username)
                    .setMaxResults(1)
                    .getResultList();

            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Override
    public List<Users> findUserByDepartmentId(int departmentId) {
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
