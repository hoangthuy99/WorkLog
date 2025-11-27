package com.ra.DAO.User;

import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAO implements IUserDAO {

    @Override
    public Users create(Users user) {
        Transaction transaction = null;
        // try-with-resources để tự động đóng session
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Thêm user vào DB
            session.save(user);
            // Commit transaction
            transaction.commit();
            System.out.println("User created successfully!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // rollback nếu lỗi
            }
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void update(Users user) {
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
    public void findById(int id) {
        //TODO:Tìm kiếm người dùng theo ID
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Users user = session.get(Users.class, id);
            if (user != null) {
                System.out.println("User found: " + user);
            } else {
                System.out.println("User not found with ID: " + id);
            }
    }catch (Exception e) {
        e.printStackTrace();}
    }
}
