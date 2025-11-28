package com.ra.Service.Auth;

import com.ra.DTO.request.UserRequest;


public interface AuthSerVice {
    UserRequest login(String username, String password);
    void logout();
    boolean checkPermission(String requiredRole);
}
