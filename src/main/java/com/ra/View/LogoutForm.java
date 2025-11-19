package com.ra.View;

import javax.swing.*;
import java.awt.*;

public class LogoutForm extends JFrame {
    private JLabel lblMessage;
    private JButton btnLogout;
    private JButton btnCancel;

    public LogoutForm() {
        setTitle("Logout Confirmation");
        setSize(350, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        lblMessage = new JLabel("Bạn có chắc muốn đăng xuất?", SwingConstants.CENTER);
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        add(lblMessage, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        btnLogout = new JButton("Đăng xuất");
        btnCancel = new JButton("Hủy");

        panelButtons.add(btnLogout);
        panelButtons.add(btnCancel);

        add(panelButtons, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LogoutForm().setVisible(true);
        });
    }
}