package com.ra.DAO.User;

import com.ra.DTO.request.UserRequest;
import com.ra.Model.Entity.Users;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    Users create(Users user);
    Users update(Users user);
    boolean deleteFindById(int id);
    List<Users> findAll(String keyword, int page, int size);
    List<Users> findAll();

    Users findById(int id);
    long countAll(String keyword);
    Optional<Users> findByUsername(String username);

}
