package com.ra.Service.User;

import com.ra.DAO.User.UserDAO;
import com.ra.DTO.request.UserRequest;
import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UserServiceIMPL implements UserService {
    private UserDAO userDAO = new UserDAO();


    @Override
    public Users create(Users user) {
        return null;
    }

    @Override
    public Users update(Users user) {
        return null;
    }

    @Override
    public boolean deleteFindById(int id) {
        return userDAO.deleteFindById(id);

    }

    @Override
    public List<Users> findAll(String keyword, int page, int size) {
        return List.of();
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
    public long countAll(String keyword) {
        return 0;
    }

    @Override
    public Optional<Users> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }



}
