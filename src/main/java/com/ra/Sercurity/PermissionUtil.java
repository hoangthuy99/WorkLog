package com.ra.Sercurity;

import com.ra.Model.Entity.Users;

public class PermissionUtil {

    public static Users CURRENT_USER;

    public static void setUser(Users user) {
        CURRENT_USER = user;
    }
    public static Users getUser() {
        return CURRENT_USER;
    }

    public static boolean hasPermission(String code) {
        if (CURRENT_USER == null) return false;
        if (CURRENT_USER.getRole() == null) return false;
        if (CURRENT_USER.getRole().getPermissions() == null) return false;

        return CURRENT_USER.getRole().getPermissions()
                .stream()
                .anyMatch(p -> p.getCode().equalsIgnoreCase(code));
    }
}
