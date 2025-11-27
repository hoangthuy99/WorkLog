package com.ra.DAO.User;

import com.ra.Model.Entity.Users;

import java.util.List;

public interface IUserDAO {
    void create(Users user);
    void update(Users user);
    boolean deleteFindById(int id);
    List<Users> findAll(String keyword, int page, int size);
}
