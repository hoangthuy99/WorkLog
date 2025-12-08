package com.ra.DAO.Holiday;

import com.ra.Model.Entity.Holidays;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class HolidayDAO implements IHolidayDAO {
    @Override
    public Holidays create(Holidays holiday) {
        //TODO : tao moi holiday
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(holiday);
            transaction.commit();
            System.out.println("Holiday created successfully!");
            return holiday;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Holidays update(Holidays holiday) {
        return null;
    }

    @Override
    public boolean deleteFindById(int id) {
        //TODO : xoa holiday bang id
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Holidays holiday = session.get(Holidays.class, id);
            if (holiday != null) {
                session.delete(holiday);
                transaction.commit();
                System.out.println("Holiday deleted successfully!");
                return true;
            } else {
                System.out.println("Holiday not found with id: " + id);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Holidays> findAll() {
        //TODO : lay tat ca holiday
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Holidays";
            return session.createQuery(hql, Holidays.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<Holidays> findById(int id) {
        //TODO : tim holiday bang id
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Holidays holiday = session.get(Holidays.class, id);
            return Optional.ofNullable(holiday);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Holidays> findByName(String name) {
        //TODO : tim holiday bang name
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Holidays h WHERE h.name = :name";
            Holidays holiday = session.createQuery(hql, Holidays.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(holiday);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Holidays> findByDate(String dateHoliday) {
        //TODO : tim holiday bang date
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Holidays h WHERE h.dateHoliday = :dateHoliday";
            return session.createQuery(hql, Holidays.class)
                    .setParameter("dateHoliday", dateHoliday)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}



