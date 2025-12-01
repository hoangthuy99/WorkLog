package com.ra.Controller;

import com.ra.DAO.Role.RoleDAO;
import com.ra.Model.Entity.Roles;

import java.util.Optional;

public class RoleController {

    private final RoleDAO roleDAO = new RoleDAO();

    /**
     * Tìm role theo tên (EMPLOYEE, MANAGER, ADMIN)
     */
    public Optional<Roles> findByName(String name) {
        return roleDAO.findByName(name);
    }

}
