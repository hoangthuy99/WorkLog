package com.ra.View;

import com.ra.View.Permission.PermissionListForm;
import com.ra.View.User.UserListForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardForm extends JFrame {

    private JButton btnUsers, btnRoles, btnPermission, btnDepartment;
    private JButton btnProject, btnTasks, btnAttendance, btnWorkRecord, btnHolidays;
    private JTable tableOverview;
    private DefaultTableModel tableModel;

    public DashboardForm() {
        setTitle("Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        // Panel chứa các nút chức năng
        JPanel panelButtons = new JPanel(new GridLayout(2, 5, 10, 10));

        btnUsers = new JButton("Users");
        btnRoles = new JButton("Roles");
        btnPermission = new JButton("Permission");
        btnDepartment = new JButton("Department");
        btnProject = new JButton("Project");
        btnTasks = new JButton("Tasks");
        btnAttendance = new JButton("Attendance");
        btnWorkRecord = new JButton("WorkRecord");
        btnHolidays = new JButton("Holidays");

        panelButtons.add(btnUsers);
        panelButtons.add(btnRoles);
        panelButtons.add(btnPermission);
        panelButtons.add(btnDepartment);
        panelButtons.add(btnProject);
        panelButtons.add(btnTasks);
        panelButtons.add(btnAttendance);
        panelButtons.add(btnWorkRecord);
        panelButtons.add(btnHolidays);

        // Panel chứa bảng tổng quan
        tableModel = new DefaultTableModel();
        tableOverview = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableOverview);

        // Layout chính
        setLayout(new BorderLayout(10, 10));
        add(panelButtons, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // TODO: thêm ActionListener cho từng nút để mở form chi tiết tương ứng
        btnUsers.addActionListener(e -> new UserListForm().setVisible(true));
//        btnPermission.addActionListener(e -> new PermissionListForm().setVisible(true));
//        btnDepartment.addActionListener(e -> new DepartmentForm().setVisible(true));
//        btnProject.addActionListener(e -> new ProjectForm().setVisible(true));
//        btnTasks.addActionListener(e -> new TasksForm().setVisible(true));
//        btnAttendance.addActionListener(e -> new AttendanceForm().setVisible(true));
//        btnWorkRecord.addActionListener(e -> new WorkRecordForm().setVisible(true));
//        btnHolidays.addActionListener(e -> new HolidaysForm().setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardForm().setVisible(true));
    }
}
