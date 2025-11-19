package com.ra.Service.User;

import com.ra.DAO.User.UserDAO;
import com.ra.Model.Entity.Users;

import java.util.List;
import java.util.Optional;

public class UserServiceIMPL implements UserService {
    private UserDAO userDAO = new UserDAO();
    @Override
    public void create() {


    }

    @Override
    public void update() {

    }

    @Override
    public boolean deleteFindById(int id) {
        return false;
    }

    @Override
    public Optional<Users> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Users> searchUsers(String keyword, int page, int size) {
        return List.of();
    }



}
