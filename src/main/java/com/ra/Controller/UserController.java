package com.ra.Controller;

import com.ra.DAO.User.UserDAO;
import com.ra.Model.Entity.Users;
import com.ra.View.User.CreateUserForm;
import com.ra.View.User.UserListForm;

import javax.swing.*;
import java.util.List;

public class UserController {
    private final UserListForm view;
    private final UserDAO userDAO;

    public UserController(UserListForm view) {
        this.view = view;
        this.userDAO = new UserDAO();

        initController();
        loadData("", 1);
    }

    private void initController() {


    }

    private void loadData(String keyword, int page) {
        List<Users> users = userDAO.findAll(keyword, page, 10);
        view.loadUserList(users);
    }

    private void searchUsers() {

    }

    private void editUser() {

    }

    private void deleteUser() {

        }


}
