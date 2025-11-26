package com.ra.Service.Auth;

import com.ra.DTO.request.UserRequest;
import com.ra.DTO.response.UserResponse;
import com.ra.Model.Entity.Users;

public interface AuthSerVice {
    UserRequest login(String username, String password);
    void logout();
    boolean checkPermission(String requiredRole);
}
