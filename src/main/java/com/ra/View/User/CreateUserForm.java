package com.ra.View.User;

import com.ra.Model.Entity.Users;
import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Department;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CreateUserForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JComboBox<Roles> cmbRole;
    private JComboBox<Department> cmbDepartment;
    private JButton btnCreate;
    private JButton btnCancel;

    public CreateUserForm() {
        setTitle("Create New User");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // khoảng cách giữa các component
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtEmail = new JTextField(20);
        txtFullName = new JTextField(20);
        cmbRole = new JComboBox<>();
        cmbDepartment = new JComboBox<>();
        btnCreate = new JButton("Create");
        btnCancel = new JButton("Cancel");

        // Load roles và departments
        loadRoles();
        loadDepartments();

        // Tiêu đề
        JLabel lblTitle = new JLabel("Create New User");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1; // reset
        gbc.gridy++;

        // Username
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Full Name
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        panel.add(txtFullName, gbc);

        // Role
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbRole, gbc);

        // Department
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbDepartment, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnCreate);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, gbc);

        btnCreate.addActionListener(e -> createUser());
        btnCancel.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }


    private void loadRoles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Roles> roles = session.createQuery("from Roles", Roles.class).list();
            for (Roles r : roles) {
                cmbRole.addItem(r);
            }
        }
    }

    private void loadDepartments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Department> departments = session.createQuery("from Department", Department.class).list();
            for (Department d : departments) {
                cmbDepartment.addItem(d);
            }
        }
    }

    private void createUser() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String email = txtEmail.getText();
        String fullName = txtFullName.getText();
        Roles role = (Roles) cmbRole.getSelectedItem();
        Department department = (Department) cmbDepartment.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hash password với BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(role);
        user.setDepartment(department);
        user.setCreatedAt(java.time.LocalDateTime.now());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            JOptionPane.showMessageDialog(this, "User created successfully!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateUserForm::new);
    }
}
