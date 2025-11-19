package com.ra.View.User;

import com.ra.DAO.User.UserDAO;
import com.ra.Model.Entity.Users;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserListForm extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JComboBox<String> cmbSortBy;
    private JComboBox<String> cmbSortOrder;


    // Biến phân trang
    private int currentPage = 1;
    private final int pageSize = 10;

    private final UserDAO userDAO = new UserDAO();


    public UserListForm() {

    }

   // fuction load user list
    public void loadUserList(List<Users> users) {
        model.setRowCount(0);

        for (Users u : users) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getFullName(),
                    u.getRole().getName()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserListForm view = new UserListForm();
            view.setVisible(true);
        });
    }





}