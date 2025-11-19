package com.ra.Service.Auth;

import com.ra.DAO.Auth.IAuthDAO;
import com.ra.DTO.request.UserRequest;
import com.ra.Model.Entity.Users;
import com.ra.Sercurity.PasswordHash;
import com.ra.Sercurity.SessionLocal;


public class AuthIMPL implements AuthSerVice {

    private IAuthDAO authDAO;

    // Inject DAO qua constructor
    public AuthIMPL(IAuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    @Override
    public UserRequest login(String username, String password) {
        Users user = authDAO.findByUsername(username);
        if(user == null){
           throw new RuntimeException("User do not exist");
        }

        if (!PasswordHash.verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }
        SessionLocal.put("currentUser", user);
        UserRequest userRequest = new UserRequest();
        userRequest.setId(user.getId());
        userRequest.setUsername(user.getUsername());
        userRequest.setEmail(user.getEmail());
       return userRequest;

    }

    @Override
    public void logout() {
        SessionLocal.remove("currentUser");
    }

    @Override
    public boolean checkPermission(String requiredRole) {
        Users currentUser = (Users) SessionLocal.get("currentUser");
        if (currentUser == null) return false;

        String requiredPermission = requiredRole.trim();

        return currentUser.getRole().getPermissions()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(requiredPermission));


    }
}