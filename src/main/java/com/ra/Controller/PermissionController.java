package com.ra.Controller;

import com.ra.DAO.Permission.IPermissionDAO;
import com.ra.DAO.Permission.PermissionDAO;
import com.ra.Model.Entity.Permission;

import java.util.List;
import java.util.Optional;

public class PermissionController {

    private final IPermissionDAO permissionDAO;

    public PermissionController() {
        this.permissionDAO = new PermissionDAO();
    }


    public Permission create(Permission permission) {
        if (permission == null) return null;
        if (permission.getCode() == null || permission.getCode().trim().isEmpty()) {
            System.out.println("Permission code cannot be empty!");
            return null;
        }
        if (permission.getName() == null || permission.getName().trim().isEmpty()) {
            System.out.println("Permission name cannot be empty!");
            return null;
        }

        return permissionDAO.save(permission);
    }


    public Permission update(Permission permission) {
        if (permission == null || permission.getId() <= 0) {
            System.out.println("Invalid permission!");
            return null;
        }
        return permissionDAO.update(permission);
    }

    public boolean delete(int id) {
        if (id <= 0) return false;
        return permissionDAO.deleteById(id);
    }

    public Optional<Permission> findById(int id) {
        return permissionDAO.findById(id);
    }


    public List<Permission> findAll() {
        return permissionDAO.findAll();
    }


    public List<Permission> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return permissionDAO.search(keyword);
    }
}
