// File: com/example/views/LoginScreen.java
package com.ra.View;

import com.ra.Common.ErrorConstants; // <-- Đã thêm import

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border; // Giữ lại

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JToggleButton togglePasswordField;
    private JButton loginButton;
    private JLabel messageLabel;

    // --- KHAI BÁO NHÃN LỖI RIÊNG BIỆT ---
    private JLabel lblUsernameError;
    private JLabel lblPasswordError;
    // ------------------------------------

    // --- CẤU HÌNH FONT VÀ MÀU (LẤY TỪ ErrorConstants) ---
    private final Font MAIN_FONT = ErrorConstants.MAIN_FONT;
    private final Font BOLD_FONT = ErrorConstants.BOLD_FONT;
    // private final Font ERROR_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 12); // <-- Đã bỏ

    private final Color PRIMARY_COLOR = ErrorConstants.PRIMARY_COLOR;
    private final Color TEXT_COLOR = ErrorConstants.TEXT_COLOR;
    // private final Color BORDER_COLOR = new Color(200, 200, 200); // <-- Đã bỏ
    // private final Color ERROR_COLOR = new Color(231, 76, 60); // <-- Đã bỏ

    // --- KHAI BÁO CÁC VIỀN CẦN THIẾT (LẤY TỪ ErrorConstants) ---
    private final Border DEFAULT_BORDER = ErrorConstants.DEFAULT_BORDER;
    private final Border ERROR_BORDER = ErrorConstants.ERROR_BORDER;

    private JPanel passwordContainer;

    // Mã Unicode cho Emoji (Chỉ dùng cho nút/icon)
    private final String EMOJI_EYE_OPEN = "\uD83D\uDC41"; // 👁
    private final String EMOJI_EYE_CLOSE = "\uD83D\uDEAB"; // 🚫

    public LoginScreen() {
        initComponents();
    }

    private void initComponents() {
        setTitle("勤怠管理システム - ログイン");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setResizable(true);
        setMinimumSize(new Dimension(450, 400));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(248, 250, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // --- 1. TITLE ---
        JLabel titleLabel = new JLabel("勤怠管理システム");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        mainPanel.add(titleLabel, gbc);

        // --- 2. SUBTITLE ---
        JLabel subtitleLabel = new JLabel("システムログイン");
        subtitleLabel.setFont(MAIN_FONT);
        subtitleLabel.setForeground(Color.GRAY);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 10);
        mainPanel.add(subtitleLabel, gbc);

        // --- RESET GBC cho FORM ---
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;

        // --- 3. USERNAME FIELD ---
        gbc.gridy = 2; // Hàng 2
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;

        JLabel usernameLabel = new JLabel("ユーザーID:");
        usernameLabel.setFont(MAIN_FONT);
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        usernameField.setFont(MAIN_FONT);

        usernameField.setBorder(new CompoundBorder(
                DEFAULT_BORDER,
                new EmptyBorder(5, 8, 5, 8)));
        mainPanel.add(usernameField, gbc);

        // --- 3b. USERNAME ERROR LABEL ---
        lblUsernameError = new JLabel(" ");
        lblUsernameError.setForeground(ErrorConstants.ERROR_COLOR); // <-- Dùng ErrorConstants
        lblUsernameError.setFont(ErrorConstants.ERROR_FONT);       // <-- Dùng ErrorConstants
        lblUsernameError.setVisible(false);

        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.insets = new Insets(-8, 10, 0, 10);
        gbc.gridwidth = 1;
        mainPanel.add(lblUsernameError, gbc);

        // --- 4. PASSWORD FIELD ---
        gbc.gridy = 4; // Hàng 4
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel passwordLabel = new JLabel("パスワード:");
        passwordLabel.setFont(MAIN_FONT);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        passwordContainer = new JPanel(new BorderLayout());
        passwordContainer.setBackground(Color.WHITE);
        passwordContainer.setBorder(DEFAULT_BORDER);

        passwordField = new JPasswordField(20);
        passwordField.setFont(MAIN_FONT);
        passwordField.setBorder(new EmptyBorder(5, 8, 5, 5));
        passwordContainer.add(passwordField, BorderLayout.CENTER);

        // Nút toggle
        togglePasswordField = new JToggleButton(EMOJI_EYE_OPEN);
        togglePasswordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));

        togglePasswordField.setPreferredSize(new Dimension(45, 30));
        togglePasswordField.setFocusPainted(false);
        togglePasswordField.setBorder(new EmptyBorder(0, 5, 0, 5));
        togglePasswordField.setContentAreaFilled(false);
        togglePasswordField.setCursor(new Cursor(Cursor.HAND_CURSOR));

        passwordContainer.add(togglePasswordField, BorderLayout.EAST);
        mainPanel.add(passwordContainer, gbc);

        // --- 4b. PASSWORD ERROR LABEL ---
        lblPasswordError = new JLabel(" ");
        lblPasswordError.setForeground(ErrorConstants.ERROR_COLOR); // <-- Dùng ErrorConstants
        lblPasswordError.setFont(ErrorConstants.ERROR_FONT);       // <-- Dùng ErrorConstants
        lblPasswordError.setVisible(false);

        gbc.gridy = 5; // Hàng 5
        gbc.gridx = 1;
        gbc.insets = new Insets(-8, 10, 0, 10);
        gbc.gridwidth = 1;
        mainPanel.add(lblPasswordError, gbc);

        // --- 5. LOGIN BUTTON ---
        loginButton = new JButton("ログイン");
        loginButton.setPreferredSize(new Dimension(200, 45));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(BOLD_FONT);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { loginButton.setBackground(new Color(41, 128, 185)); }
            public void mouseExited(MouseEvent e) { loginButton.setBackground(PRIMARY_COLOR); }
        });

        gbc.gridx = 0;
        gbc.gridy = 6; // Hàng 6
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(loginButton, gbc);

        // --- 6. MESSAGE LABEL ---
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(ErrorConstants.ERROR_COLOR); // <-- Dùng ErrorConstants
        messageLabel.setFont(ErrorConstants.MESSAGE_FONT);      // <-- Dùng ErrorConstants
        gbc.gridy = 7; // Hàng 7
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(messageLabel, gbc);

        // --- 7. INFO LABEL (Footer) ---
        JLabel infoLabel = new JLabel("<html><center><span style='color:#95a5a6'>Tài khoản demo:</span><br>" +
                "employee / supervisor / admin</center></html>");
        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        gbc.weighty = 1.0;
        gbc.gridy = 8; // Hàng 8
        mainPanel.add(infoLabel, gbc);

        add(mainPanel);

        // --- ACTIONS ---
        loginButton.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        togglePasswordField.addActionListener(e -> {
            JToggleButton btn = (JToggleButton) e.getSource();
            if (btn.isSelected()) {
                passwordField.setEchoChar((char) 0);
                btn.setText(EMOJI_EYE_CLOSE);
            } else {
                passwordField.setEchoChar('•');
                btn.setText(EMOJI_EYE_OPEN);
            }
        });

        passwordField.setEchoChar('•');
    }

    private void resetErrorLabelsAndBorders() {
        lblUsernameError.setVisible(false);
        lblPasswordError.setVisible(false);
        messageLabel.setText(" ");

        // Khôi phục viền mặc định
        usernameField.setBorder(new CompoundBorder(DEFAULT_BORDER, new EmptyBorder(5, 8, 5, 8)));
        passwordContainer.setBorder(DEFAULT_BORDER);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        boolean hasInputError = false;

        resetErrorLabelsAndBorders();

        // 1. Kiểm tra Lỗi nhập liệu (Thiếu trường)
        if (username.isEmpty()) {
            lblUsernameError.setText(ErrorConstants.ERROR_MESSAGE_REQUIRED_USERNAME); // <-- Dùng ErrorConstants
            lblUsernameError.setVisible(true);
            usernameField.setBorder(new CompoundBorder(ERROR_BORDER, new EmptyBorder(5, 8, 5, 8)));
            hasInputError = true;
        }

        if (password.isEmpty()) {
            lblPasswordError.setText(ErrorConstants.ERROR_MESSAGE_REQUIRED_PASSWORD); // <-- Dùng ErrorConstants
            lblPasswordError.setVisible(true);
            passwordContainer.setBorder(ERROR_BORDER);
            hasInputError = true;
        }

        if (hasInputError) {
            messageLabel.setText(ErrorConstants.ERROR_MESSAGE_INPUT_MISSING); // <-- Dùng ErrorConstants
            return;
        }


        // 2. Logic Demo (Thay thế cho Controller/Backend)

        // Tài khoản demo duy nhất: "admin" và "123456"
        if (username.equals("admin") && password.equals("123456")) {

            // --- THAY ĐỔI LỚN TẠI ĐÂY: MỞ ADMIN DASHBOARD ---
            this.dispose(); // Đóng màn hình LoginScreen hiện tại
            SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true)); // Khởi tạo và hiển thị AdminDashboard
            // ----------------------------------------------------

        } else {
            // Lỗi xác thực (Sai User ID hoặc Password)
            lblUsernameError.setText(ErrorConstants.ERROR_MESSAGE_AUTH_FAILED); // <-- Dùng ErrorConstants
            lblUsernameError.setVisible(true);

            // Highlight cả hai trường nếu lỗi xác thực
            usernameField.setBorder(new CompoundBorder(ERROR_BORDER, new EmptyBorder(5, 8, 5, 8)));
            passwordContainer.setBorder(ERROR_BORDER);

            messageLabel.setText(ErrorConstants.ERROR_MESSAGE_LOGIN_FAILED); // <-- Dùng ErrorConstants
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}