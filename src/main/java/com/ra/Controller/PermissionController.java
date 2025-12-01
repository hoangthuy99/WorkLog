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

    /** =============================
     *        CREATE
     *  ============================= */
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

    /** =============================
     *        UPDATE
     *  ============================= */
    public Permission update(Permission permission) {
        if (permission == null || permission.getId() <= 0) {
            System.out.println("Invalid permission!");
            return null;
        }
        return permissionDAO.update(permission);
    }

    /** =============================
     *        DELETE
     *  ============================= */
    public boolean delete(int id) {
        if (id <= 0) return false;
        return permissionDAO.deleteById(id);
    }

    /** =============================
     *       FIND BY ID
     *  ============================= */
    public Optional<Permission> findById(int id) {
        return permissionDAO.findById(id);
    }

    /** =============================
     *        FIND ALL
     *  ============================= */
    public List<Permission> findAll() {
        return permissionDAO.findAll();
    }

    /** =============================
     *        SEARCH
     *  ============================= */
    public List<Permission> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return permissionDAO.search(keyword);
    }
}
