package com.ra.View;

import com.ra.DAO.Auth.AuthDAO;
import com.ra.DTO.request.UserRequest;
import com.ra.Service.Auth.AuthIMPL;
import com.ra.Service.Auth.AuthSerVice;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;

    private AuthSerVice authService = new AuthIMPL(new AuthDAO());

    public LoginForm() {
        setTitle("Login Form");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("User Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(lblUsername, gbc);

        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancel");
        panelButtons.add(btnLogin);
        panelButtons.add(btnCancel);

        add(panelButtons, BorderLayout.SOUTH);

        // Sự kiện Login
        btnLogin.addActionListener(e -> handleLogin());

        btnCancel.addActionListener(e -> System.exit(0));
    }

    private void handleLogin() {
        try {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserRequest loggedUser = authService.login(username, password);

            JOptionPane.showMessageDialog(this, "Login thành công! Xin chào " + loggedUser.getUsername());

            // Mở dashboard
            new DashboardForm().setVisible(true);

            dispose(); // đóng form login
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
