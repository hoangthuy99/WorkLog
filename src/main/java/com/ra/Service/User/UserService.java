package com.ra.Service.User;

import com.ra.Model.Entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService  {
 void create();
 void update();
 boolean deleteFindById(int id);
 Optional<Users> findById(int id);
 List<Users> searchUsers(String keyword, int page, int size);
}
