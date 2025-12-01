package com.ra.DAO.Role;

import com.ra.Model.Entity.Roles;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class RoleDAO implements IRoleDAO {

    @Override
    public Roles findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Roles.class, id);
        }
    }

    @Override
    public Optional<Roles> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Roles role = session.createQuery(
                            "FROM Roles r WHERE r.name = :name", Roles.class
                    ).setParameter("name", name)
                    .uniqueResult();

            return Optional.ofNullable(role);
        }
    }


    @Override
    public List<Roles> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Roles", Roles.class).list();
        }
    }
}
