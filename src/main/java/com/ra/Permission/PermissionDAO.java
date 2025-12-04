package com.ra.Permission;

import com.ra.DAO.Permission.IPermissionDAO;
import com.ra.Model.Entity.Permission;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PermissionDAO implements IPermissionDAO {

    @Override
    public Permission save(Permission permission) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(permission);
            tx.commit();
            return permission;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Permission update(Permission permission) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(permission);
            tx.commit();
            return permission;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteById(int id) {

        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();
            Permission p = session.get(Permission.class, id);

            if (p != null) {
                session.remove(p);
                tx.commit();
                return true;
            }

            return false;

        } catch (Exception e) {

            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;

        }
    }

    @Override
    public Optional<Permission> findById(int id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Permission p = session.createQuery(
                            "SELECT DISTINCT p FROM Permission p " +
                                    "LEFT JOIN FETCH p.roles " +
                                    "WHERE p.id = :id", Permission.class)
                    .setParameter("id", id)
                    .uniqueResult();

            return Optional.ofNullable(p);

        } catch (Exception e) {

            e.printStackTrace();
            return Optional.empty();

        }
    }

    @Override
    public List<Permission> findAll() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT DISTINCT p FROM Permission p " +
                            "LEFT JOIN FETCH p.roles",
                    Permission.class
            ).list();

        } catch (Exception e) {

            e.printStackTrace();
            return List.of();

        }
    }

    @Override
    public List<Permission> search(String keyword) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "SELECT DISTINCT p FROM Permission p " +
                                    "LEFT JOIN FETCH p.roles " +
                                    "WHERE p.code LIKE :kw OR p.name LIKE :kw",
                            Permission.class
                    ).setParameter("kw", "%" + keyword + "%")
                    .list();

        } catch (Exception e) {

            e.printStackTrace();
            return List.of();

        }
    }
}
