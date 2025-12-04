package com.ra.DAO.Attendance;

import com.ra.Model.Entity.Attendance;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class AttendanceDAO implements IAttendanceDAO {

    @Override
    public void create(Attendance attendance) {
        Transaction t = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            t = s.beginTransaction();
            s.save(attendance);
            t.commit();
        } catch (Exception e) {
            if (t != null) t.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Attendance attendance) {
        Transaction t = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            t = s.beginTransaction();
            s.update(attendance);
            t.commit();
        } catch (Exception e) {
            if (t != null) t.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Attendance attendance) {
        Transaction t = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            t = s.beginTransaction();
            s.delete(attendance);
            t.commit();
            return true;
        } catch (Exception e) {
            if (t != null) t.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Attendance> findById(int id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(Attendance.class, id));
        }
    }

    @Override
    public List<Attendance> findAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Attendance", Attendance.class).list();
        }
    }

    @Override
    public List<Attendance> search(String keyword, int page, int size) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "SELECT a FROM Attendance a WHERE a.user.userName LIKE :kw",
                            Attendance.class
                    )
                    .setParameter("kw", "%" + keyword + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();
        }
    }

    @Override
    public Attendance findByUsername(String username) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Attendance a WHERE a.user.userName = :u",
                            Attendance.class
                    )
                    .setParameter("u", username)
                    .uniqueResult();
        }
    }
}
