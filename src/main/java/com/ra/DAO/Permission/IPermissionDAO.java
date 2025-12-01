package com.ra.DAO.Permission;

import com.ra.Model.Entity.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionDAO {

    Permission save(Permission permission);

    Permission update(Permission permission);

    boolean deleteById(int id);

    Optional<Permission> findById(int id);

    List<Permission> findAll();

    List<Permission> search(String keyword);
}
