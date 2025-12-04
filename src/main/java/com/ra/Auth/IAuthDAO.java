package com.ra.Auth;

import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Users;

import java.util.List;
import java.util.Optional;

public interface IAuthDAO
{
    Users findByUsername(String username);
    List<Roles> findAllRoles();
    Optional<Roles> findByName(String roleName);
}