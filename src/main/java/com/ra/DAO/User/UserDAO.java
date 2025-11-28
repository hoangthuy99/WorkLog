package com.ra.DAO.User;

import com.ra.DTO.request.UserRequest;
import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UserDAO implements IUserDAO {

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
            String hql = "SELECT COUNT(u) FROM Users u WHERE u.fullName LIKE :kw OR u.userName LIKE :kw";
            return (long) session.createQuery(hql)
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
                session.delete(user);
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
            String hql = "FROM Users u WHERE u.userName LIKE :keyword OR u.email LIKE :keyword";
            return session.createQuery(hql, Users.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<Users> findById(int id) {
        return Optional.empty();
    }


}
