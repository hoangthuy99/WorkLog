package com.ra.Role;

import com.ra.Model.Entity.Roles;

import java.util.List;
import java.util.Optional;

public interface IRoleDAO {
    Roles findById(int id);
    Optional<Roles> findByName(String name);
    List<Roles> findAll();

}
