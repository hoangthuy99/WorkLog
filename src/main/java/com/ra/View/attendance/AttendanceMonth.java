/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.ra.View.attendance;

import com.ra.Controller.AttendanceController;
import com.ra.Controller.HolidayController;
import com.ra.Controller.UserController;
import com.ra.DAO.Department.DepartmentDAO;
import com.ra.DAO.Holiday.HolidayDAO;
import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Users;
import com.ra.View.dashboard.MainDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author thuyhoang
 */
public class AttendanceMonth extends javax.swing.JPanel {

    /**
     * Creates new form AttendanceMonth
     */
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AttendanceMonth.class.getName());
    private Users loggedInUser;
    private AttendanceController attendanceController;
    private UserController userController;
    HolidayController holidayController = new HolidayController(new HolidayDAO());
    public AttendanceMonth(Users user) {
        initComponents();
        this.loggedInUser = user;
        this.attendanceController = new AttendanceController();
        this.userController = new UserController();

        // Center table columns
        centerTableColumns(tblAttendanceDate);
        tblAttendanceDate.setShowGrid(true);
        tblAttendanceDate.setGridColor(new Color(220, 220, 220));

        loadDepartments();
        LoadUsers();

        // ➜ Ẩn các nút / control không dành cho employee
        setupUIByRole();

        // (option) nếu muốn tự động load tháng hiện tại cho employee:
        // if (!isManager(loggedInUser)) {
        //     btnSearchMonthActionPerformed(null);
        // }
    }


    // Get users based on logged-in user's role
    private List<Users> getUsersBasedOnRole() {
        List<Users> users = new ArrayList<>();

        if (loggedInUser == null) {
            // If no user logged in, show all (for testing)
            users = userController.findAll();
        } else if (isManager(loggedInUser)) {
            // Manager sees all users
            users = userController.findAll();
        } else {
            // Employee sees only themselves
            users.add(loggedInUser);
        }

        return users;
    }
    // Check if the user has a manager or admin role
    private boolean isManager(Users user) {
        if (user == null || user.getRole() == null) return false;
        int roleId = user.getRole().getId();   // 1=EMP, 2=MANAGER, 3=ADMIN
        return roleId == 2 || roleId == 3;
    }

    private void setupUIByRole() {
        // Nếu KHÔNG phải Manager/Admin => Employee
        if (!isManager(loggedInUser)) {
            // Ẩn 2 nút検索
            btnSearchUser.setVisible(false);

            // Ẩn luôn combobox + label chọn user/department
            cbUserNo.setVisible(false);
            cbDepart.setVisible(false);
            jLabel1.setVisible(false); // 社員No
            jLabel2.setVisible(false); // 部署
        }
    }


    private String getStatusJapanese(int status) {
        switch (status) {
            case 0: return "未確認";
            case 1: return "確認済み";
            case 2: return "拒否済み";
            default: return "不明";
        }
    }
    private void adjustColumnWidths() {
        // 0 = ID (ẩn), 1 = No, 2 = 社員名 ...
        if (tblAttendanceDate.getColumnCount() < 10) return;

        tblAttendanceDate.getColumnModel().getColumn(1).setPreferredWidth(50);   // No
        tblAttendanceDate.getColumnModel().getColumn(2).setPreferredWidth(120);  // 社員名
        tblAttendanceDate.getColumnModel().getColumn(3).setPreferredWidth(60);   // 曜日
        tblAttendanceDate.getColumnModel().getColumn(4).setPreferredWidth(80);   // 開始
        tblAttendanceDate.getColumnModel().getColumn(5).setPreferredWidth(80);   // 終了
        tblAttendanceDate.getColumnModel().getColumn(6).setPreferredWidth(100);  // 勤務時間
        tblAttendanceDate.getColumnModel().getColumn(7).setPreferredWidth(80);   // 休憩
        tblAttendanceDate.getColumnModel().getColumn(8).setPreferredWidth(80);   // 残業
        tblAttendanceDate.getColumnModel().getColumn(9).setPreferredWidth(100);  // 状態
    }

    private void centerTableColumns(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblAttendanceDate = new javax.swing.JTable();
        btnSearchUser = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        cbMonth = new com.toedter.calendar.JMonthChooser();
        btnSearchMonth = new javax.swing.JButton();
        cbUserNo = new javax.swing.JComboBox<>();
        cbDepart = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbYear = new com.toedter.calendar.JYearChooser();

        tblAttendanceDate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "曜日", "開始", "終了", "休憩", "残業", "状態"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAttendanceDate);
        if (tblAttendanceDate.getColumnModel().getColumnCount() > 0) {
            tblAttendanceDate.getColumnModel().getColumn(0).setResizable(false);
            tblAttendanceDate.getColumnModel().getColumn(1).setResizable(false);
            tblAttendanceDate.getColumnModel().getColumn(2).setResizable(false);
            tblAttendanceDate.getColumnModel().getColumn(3).setResizable(false);
            tblAttendanceDate.getColumnModel().getColumn(4).setResizable(false);
            tblAttendanceDate.getColumnModel().getColumn(5).setResizable(false);
            tblAttendanceDate.getColumnModel().getColumn(6).setResizable(false);
        }

        btnSearchUser.setText("検索");
        btnSearchUser.addActionListener(this::btnSearchUserActionPerformed);

        btnView.setBackground(new java.awt.Color(204, 204, 255));
        btnView.setText("詳細");
        btnView.addActionListener(this::btnViewActionPerformed);

        btnSearchMonth.setText("検索");
        btnSearchMonth.addActionListener(this::btnSearchMonthActionPerformed);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel1.setText("社員No");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel2.setText("部署");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnView)
                                .addGap(13, 13, 13)))
                        .addGap(62, 62, 62))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSearchMonth)
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbUserNo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(33, 33, 33)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbDepart, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearchUser)
                        .addContainerGap(84, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSearchUser)
                                .addComponent(cbUserNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSearchMonth))
                            .addComponent(cbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cbYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addGap(29, 29, 29)
                .addComponent(btnView)
                .addGap(53, 53, 53))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loadDepartments() {
        try {
            DepartmentDAO departmentDAO = new DepartmentDAO();
            List<Department> departments = departmentDAO.findAll();
            cbDepart.removeAllItems();
            cbDepart.addItem("未選択");
            for (Department d : departments) {
                cbDepart.addItem(d.getName());
            }
            cbDepart.setSelectedIndex(0);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Department", e);
        }
    }

    private void LoadUsers() {
        try {
            List<Users> users = userController.findAll();
            cbUserNo.removeAllItems();
            cbUserNo.addItem("未選択");
            for (Users u : users) {
                cbUserNo.addItem(u.getUserName());
            }
            cbUserNo.setSelectedIndex(0);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Users", e);
        }
    }

    private void colorSummaryRows() {
        tblAttendanceDate.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                // Cột 2 = 社員名 (sau khi thêm ID,No)
                String employeeName = null;
                Object val = table.getValueAt(row, 2);
                if (val != null) {
                    employeeName = val.toString();
                }

                if (employeeName != null && employeeName.contains("合計")) {
                    c.setBackground(new Color(220, 240, 255)); // xanh nhạt
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else if (employeeName != null && employeeName.equals("総合計")) {
                    c.setBackground(new Color(255, 240, 220)); // cam nhạt
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                return c;
            }
        });
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        int row = tblAttendanceDate.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "レコードを選択してください。");
            return;
        }

        try {
            // Lấy ID attendance từ cột 0 (đang ẩn)
            int attendanceId = (int) tblAttendanceDate.getValueAt(row, 0);

            // Lấy attendance từ DB
            Attendance attendance = attendanceController.findById(attendanceId);

            if (attendance == null) {
                JOptionPane.showMessageDialog(this, "データが存在しません。");
                return;
            }

            List<Attendance> list = new ArrayList<>();
            list.add(attendance);

            // ====== QUY ĐỊNH MODE EDIT / VIEW ======
            boolean editable = false;
            int status = attendance.getStatus();   // 0=pending,1=approved,2=rejected

            // Manager/Admin -> luôn được quyền chỉnh sửa
            if (isManager(loggedInUser)) {
                editable = true;
            }
            // Employee -> chỉ được sửa khi Pending hoặc Rejected
            else if (status == 0 || status == 2) {
                editable = true;
            }
            // status == 1 (確認済み) -> employee chỉ được xem, không sửa

            // Tìm MainDashboard
            MainDashboard mainDashboard = findParentMainDashboard(this);

            if (mainDashboard != null) {
                // viewMode = !editable
                AddAttendance addAttendancePanel =
                        new AddAttendance(mainDashboard.currentUser, list, !editable);
                mainDashboard.showPanel(addAttendancePanel);

                JOptionPane.showMessageDialog(this,
                        editable
                                ? "編集可能モードで開きました。"
                                : "閲覧モード：修正できません。",
                        "情報",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "メイン画面が見つかりません。");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "エラー: " + e.getMessage());
        }
    }//GEN-LAST:event_btnViewActionPerformed


    private MainDashboard findParentMainDashboard(Component component) {
        Container parent = component.getParent();
        while (parent != null) {
            if (parent instanceof MainDashboard) {
                return (MainDashboard) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
    private String getJapaneseDayOfWeek(LocalDate date) {
        if (date == null) return "";

        // LocalDate.getDayOfWeek() trả về DayOfWeek enum
        switch (date.getDayOfWeek()) {
            case MONDAY:    return "月";
            case TUESDAY:   return "火";
            case WEDNESDAY: return "水";
            case THURSDAY:  return "木";
            case FRIDAY:    return "金";
            case SATURDAY:  return "土";
            case SUNDAY:    return "日";
            default:        return "";
        }
    }
   
    private String formatMinutesToHours(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%d時間%d分", hours, mins);
    }

    private void btnSearchMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchMonthActionPerformed
        try {
            // Lấy tháng và năm
            int month = cbMonth.getMonth() + 1;
            int year = cbYear.getYear();

            System.out.println("Searching for month: " + month + "/" + year);

            // Tạo table model với cột có ID (ẩn)
            String[] columns = {"ID", "No", "社員名", "曜日", "開始", "終了", "勤務時間", "休憩", "残業", "状態"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 0 || columnIndex == 1) return Integer.class; // ID, No
                    return String.class;
                }
            };

            // Lấy danh sách user theo role (manager thấy tất cả, staff chỉ thấy mình)
            List<Users> users = getUsersBasedOnRole();

            if (users == null || users.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ユーザーが見つかりません。");
                return;
            }

            int index = 1;
            int totalWorkMinutes = 0;
            int totalOvertimeMinutes = 0;

            // Duyệt qua từng user
            for (Users user : users) {
                System.out.println("Processing user: " + user.getUserName());

                // Lấy attendance của user trong tháng
                List<Attendance> attendances = attendanceController.findByAttendanceMonth(
                        user.getId(), month, year);

                System.out.println("Found " + (attendances != null ? attendances.size() : 0) + " attendances");

                if (attendances != null && !attendances.isEmpty()) {
                    // Tính tổng cho user này
                    int userWorkMinutes = 0;
                    int userOvertimeMinutes = 0;

                    for (Attendance att : attendances) {
                        System.out.println("Processing attendance ID: " + att.getId());

                        String dayOfWeek = getJapaneseDayOfWeek(att.getWorkDate());

                        // Format giờ
                        String startTime = att.getCheckInTime() != null ?
                                att.getCheckInTime().toString().substring(0, 5) : ""; // HH:mm
                        String endTime = att.getCheckOutTime() != null ?
                                att.getCheckOutTime().toString().substring(0, 5) : "";

                        String workTime = formatMinutesToHours(att.getTotalMinutes());
                        String breakTime = att.getBreakMinutes() + "分";
                        String overtime = att.getOvertimeMinutes() + "分";
                        String status = getStatusJapanese(att.getStatus());

                        // Thêm dòng chi tiết (có ID attendance)
                        model.addRow(new Object[]{
                                att.getId(),            // ID (ẩn)
                                index++,                // No
                                user.getUserName(),     // 社員名
                                dayOfWeek,              // 曜日
                                startTime,              // 開始
                                endTime,                // 終了
                                workTime,               // 勤務時間
                                breakTime,              // 休憩
                                overtime,               // 残業
                                status                  // 状態
                        });

                        userWorkMinutes += att.getTotalMinutes();
                        userOvertimeMinutes += att.getOvertimeMinutes();
                    }

                    // Dòng tổng cho từng user
                    model.addRow(new Object[]{
                            "",                                // ID
                            "",                                // No
                            user.getUserName() + " 合計",      // 社員名
                            "", "", "",
                            formatMinutesToHours(userWorkMinutes), // 勤務時間 合計
                            "",                                // 休憩
                            formatMinutesToHours(userOvertimeMinutes), // 残業 合計
                            ""                                 // 状態
                    });

                    totalWorkMinutes += userWorkMinutes;
                    totalOvertimeMinutes += userOvertimeMinutes;
                }
            }

            // Gán model cho table
            tblAttendanceDate.setModel(model);
            centerTableColumns(tblAttendanceDate);

            // Ẩn cột ID
            tblAttendanceDate.getColumnModel().getColumn(0).setMinWidth(0);
            tblAttendanceDate.getColumnModel().getColumn(0).setMaxWidth(0);
            tblAttendanceDate.getColumnModel().getColumn(0).setWidth(0);

            // Dòng tổng cuối cùng (tất cả user)
            if (model.getRowCount() > 0) {
                model.addRow(new Object[]{
                        "",          // ID
                        "",          // No
                        "総合計",    // 社員名
                        "", "", "",
                        formatMinutesToHours(totalWorkMinutes),   // 勤務時間 総合計
                        "",
                        formatMinutesToHours(totalOvertimeMinutes), // 残業 総合計
                        "全 " + (index - 1) + " 件"
                });
            }

            adjustColumnWidths();
            colorSummaryRows();

            if (index == 1) {
                JOptionPane.showMessageDialog(this,
                        month + "月 " + year + "年の勤怠データはありません。",
                        "情報",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            System.out.println("Search completed. Total records: " + (index - 1));

        } catch (Exception e) {
            System.err.println("Error in btnSearchMonthActionPerformed:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "検索中にエラーが発生しました: " + e.getMessage(),
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchMonthActionPerformed
//GEN-LAST:event_btnSearchMonthActionPerformed

    private void btnSearchUserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSearchUserActionPerformed
        try {
            // Lấy giá trị từ controls
            String selectedUserName = (String) cbUserNo.getSelectedItem();
            String selectedDeptName = (String) cbDepart.getSelectedItem();
            int month = cbMonth.getMonth() + 1;
            int year = cbYear.getYear();

            // Kiểm tra điều kiện
            if ("未選択".equals(selectedUserName) && "未選択".equals(selectedDeptName)) {
                JOptionPane.showMessageDialog(this,
                        "少なくとも1つの検索条件を選択してください（社員Noまたは部署）",
                        "検索エラー",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Attendance> searchResults = new ArrayList<>();
            String searchCriteria = "";

            // 🔹 Tìm kiếm theo user name
            if (!"未選択".equals(selectedUserName)) {

                Optional<Users> userOpt = userController.findByUsername(selectedUserName);
                if (userOpt.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "社員が見つかりません: " + selectedUserName,
                            "エラー",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Users targetUser = userOpt.get(); // nhân viên được chọn trong combobox

                if (isManager(loggedInUser)) {
                    // ✅ Manager / Admin: xem được bất kỳ nhân viên nào
                    searchResults = attendanceController.findByUsernameAndMonth(
                            targetUser.getUserName(), month, year);
                    searchCriteria = "社員: " + targetUser.getUserName();
                } else {
                    // 🔐 Employee: chỉ xem được chính mình
                    if (targetUser.getId() != loggedInUser.getId()) {
                        JOptionPane.showMessageDialog(this,
                                "他のユーザーのデータを表示する権限がありません",
                                "権限エラー",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    searchResults = attendanceController.findByUsernameAndMonth(
                            loggedInUser.getUserName(), month, year);
                    searchCriteria = "社員: " + loggedInUser.getUserName();
                }
            }
            // 🔹 Tìm kiếm theo department
            else if (!"未選択".equals(selectedDeptName)) {
                if (isManager(loggedInUser)) {
                    // ✅ Manager / Admin: xem được tất cả nhân viên trong department
                    searchResults = attendanceController.findByDepartmentAndMonth(
                            selectedDeptName, month, year);
                    searchCriteria = "部署: " + selectedDeptName;
                } else {
                    // 🔐 Employee: chỉ xem bản thân, dù chọn department nào
                    searchResults = attendanceController.findByUsernameAndMonth(
                            loggedInUser.getUserName(), month, year);
                    searchCriteria = "部署: " + selectedDeptName + " (自分のみ)";
                }
            }

            // Hiển thị kết quả
            displaySearchResults(searchResults, searchCriteria, month, year);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "検索中にエラーが発生しました: " + e.getMessage(),
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSearchUserActionPerformed


    private void displaySearchResults(List<Attendance> attendances,
                                      String searchCriteria, int month, int year) {
        if (attendances == null || attendances.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    month + "月 " + year + "年の勤怠データはありません。" +
                            (searchCriteria.isEmpty() ? "" : "\n検索条件: " + searchCriteria),
                    "検索結果",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Tạo model với cột ID
        String[] columns = {"ID", "No", "社員名", "部署", "曜日", "開始", "終了",
                "勤務時間", "休憩", "残業", "状態"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 1) return Integer.class;
                return String.class;
            }
        };

        // Nhóm theo user
        Map<Users, List<Attendance>> attendanceByUser = attendances.stream()
                .collect(Collectors.groupingBy(Attendance::getUser));

        int index = 1;
        int totalWorkMinutes = 0;
        int totalOvertimeMinutes = 0;

        for (Map.Entry<Users, List<Attendance>> entry : attendanceByUser.entrySet()) {
            Users user = entry.getKey();
            List<Attendance> userAttendances = entry.getValue();

            int userWorkMinutes = 0;
            int userOvertimeMinutes = 0;

            for (Attendance att : userAttendances) {
                String dayOfWeek = getJapaneseDayOfWeek(att.getWorkDate());
                String deptName = att.getUser().getDepartment() != null ?
                        att.getUser().getDepartment().getName() : "N/A";

                String startTime = att.getCheckInTime() != null ?
                        att.getCheckInTime().toString().substring(0, 5) : "";
                String endTime = att.getCheckOutTime() != null ?
                        att.getCheckOutTime().toString().substring(0, 5) : "";

                String workTime = formatMinutesToHours(att.getTotalMinutes());
                String breakTime = att.getBreakMinutes() + "分";
                String overtime = att.getOvertimeMinutes() + "分";
                String status = getStatusJapanese(att.getStatus());

                model.addRow(new Object[]{
                        att.getId(),           // ID (ẩn)
                        index++,               // No
                        user.getUserName(),    // 社員名
                        deptName,              // 部署
                        dayOfWeek,             // 曜日
                        startTime,             // 開始
                        endTime,               // 終了
                        workTime,              // 勤務時間
                        breakTime,             // 休憩
                        overtime,              // 残業
                        status                 // 状態
                });

                userWorkMinutes += att.getTotalMinutes();
                userOvertimeMinutes += att.getOvertimeMinutes();
            }

            // Dòng tổng cho user
            model.addRow(new Object[]{
                    "",
                    "",
                    user.getUserName() + " 合計",
                    "",
                    "",
                    "",
                    formatMinutesToHours(userWorkMinutes),
                    "",
                    formatMinutesToHours(userOvertimeMinutes),
                    userAttendances.size() + " 件"
            });

            totalWorkMinutes += userWorkMinutes;
            totalOvertimeMinutes += userOvertimeMinutes;
        }

        tblAttendanceDate.setModel(model);
        centerTableColumns(tblAttendanceDate);

        // Ẩn cột ID
        tblAttendanceDate.getColumnModel().getColumn(0).setMinWidth(0);
        tblAttendanceDate.getColumnModel().getColumn(0).setMaxWidth(0);
        tblAttendanceDate.getColumnModel().getColumn(0).setWidth(0);

        adjustColumnWidths();
        colorSummaryRows();

        JOptionPane.showMessageDialog(this,
                attendances.size() + " 件の勤怠データが見つかりました\n" +
                        "検索条件: " + searchCriteria + "\n" +
                        "期間: " + month + "月 " + year + "年",
                "検索結果",
                JOptionPane.INFORMATION_MESSAGE);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearchMonth;
    private javax.swing.JButton btnSearchUser;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox<String> cbDepart;
    private com.toedter.calendar.JMonthChooser cbMonth;
    private javax.swing.JComboBox<String> cbUserNo;
    private com.toedter.calendar.JYearChooser cbYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAttendanceDate;
    // End of variables declaration//GEN-END:variables
}
