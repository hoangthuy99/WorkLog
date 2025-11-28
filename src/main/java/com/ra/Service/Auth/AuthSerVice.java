package com.ra.Service.Auth;

import com.ra.DTO.request.UserRequest;
import com.ra.Model.Entity.Roles;

import java.util.List;


public interface AuthSerVice {
    UserRequest login(String username, String password);
    void logout();
    boolean checkPermission(String requiredRole);
    List<Roles> findAllRoles();
}
