package com.ra.Service.User;

import com.ra.DAO.User.UserDAO;
import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UserServiceIMPL implements UserService {
    private UserDAO userDAO = new UserDAO();
    @Override
    public void create() {
       userDAO.create(new Users());
    }

    @Override
    public void update() {
      userDAO.update(new Users());
    }

    @Override
    public boolean deleteFindById(int id) {
        return userDAO.deleteFindById(id);

    }

    @Override
    public Optional<Users> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Users user = session.get(Users.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Users> searchUsers(String keyword, int page, int size) {
        return userDAO.findAll(keyword, page, size);
    }

}
