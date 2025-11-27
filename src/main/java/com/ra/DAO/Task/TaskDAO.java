package com.ra.DAO.Task;

import com.ra.Model.Entity.Tasks;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TaskDAO implements ITaskDAO{
    @Override
    public void create(Tasks task) {
        //TODO: Tạo nội dung công việc vào DB
        Transaction transaction = null;
        // try-with-resources để tự động đóng session
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Thêm task vào DB
            session.save(task);
            // Commit transaction
            transaction.commit();
            System.out.println("Task created successfully!");
        }catch (Exception e){
            if (transaction != null){
                transaction.rollback(); // rollback nếu lỗi
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Tasks task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(task);
            transaction.commit();
            System.out.println("Task updated successfully!");
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
            Tasks task = session.get(Tasks.class, id);
            if (task != null) {
                session.remove(task);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }
        return false;
    }

    @Override
    public Optional<Tasks> findFindById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Tasks task = session.get(Tasks.class, id);
            return Optional.ofNullable(task);
        }
    }

    @Override
    public List<Tasks> search(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Tasks t WHERE t.name LIKE :kw OR t.taskCode LIKE :kw", Tasks.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .getResultList();
        }
    }

    @Override
    public List<Tasks> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Tasks", Tasks.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }



}