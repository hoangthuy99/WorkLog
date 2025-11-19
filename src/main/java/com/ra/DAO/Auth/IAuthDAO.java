package com.ra.DAO.Auth;

import com.ra.Model.Entity.Users;

public interface IAuthDAO
{
    Users findByUsername(String username);

}
