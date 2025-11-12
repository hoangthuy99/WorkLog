package com.ra.Model.DTO;

import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserDAO {
    public Users login(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Users> query = session.createQuery("FROM Users WHERE username = :u", Users.class);
            query.setParameter("u", username);
            Users user = query.uniqueResult();

            if (user != null && org.mindrot.jbcrypt.BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
            return null;
        }
    }
}
