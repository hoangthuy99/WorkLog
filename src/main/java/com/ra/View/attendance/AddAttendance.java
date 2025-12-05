/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.ra.View.attendance;

import com.ra.Common.Constant;
import com.ra.Controller.*;
import com.ra.DAO.Holiday.HolidayDAO;
import com.ra.DAO.Project.ProjectDAO;
import com.ra.DAO.Task.TaskDAO;
import com.ra.Model.Entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author thuyhoang
 */
public class AddAttendance extends javax.swing.JPanel {

    /**
     * Creates new form AddAttendance
     */

    private Users loggedInUser;
    private Constant constant = new Constant();
    private AttendanceController attendanceController = new AttendanceController();
    private RecordController recordController ;
    private TaskController taskController ;
    private ProjectController projectController ;
    public AddAttendance(Users user) {
        initComponents();
        initComponents();
        this.loggedInUser = user;
        this.recordController = new RecordController();
        loadUserInfo();
        loadProjects();
        loadTasks();
        initStatusComboBox();
    }

    private List<Attendance> getCurrentAttendance(int userId, LocalDate date) {
        return attendanceController.findByUserAndDate(userId, date);
    }
    private void initStatusComboBox() {
        cbStatus.removeAllItems();
        cbStatus.addItem("未確認");   // Status 0
        cbStatus.addItem("確認済み"); // Status 1
        cbStatus.addItem("拒否済み"); // Status 2
    }
    private void loadWorkRecordTable(int attendanceId) {

        List<WorkRecord> list = recordController.findByAttendanceId(attendanceId);

        DefaultTableModel model = (DefaultTableModel) tblRecord.getModel();
        model.setRowCount(0); // clear table

        int maxRecords = Math.min(list.size(), 20);

        for (int i = 0; i < maxRecords; i++) {

            WorkRecord wr = list.get(i);

            // Convert status
            String statusText;
            if (wr.getStatus() == 0) {
                statusText = "未確認";   // chưa xác nhận
            } else if (wr.getStatus() == 1) {
                statusText = "確認済み"; // đã xác nhận
            } else {
                statusText = "拒否済み"; // từ chối
            }

            Object[] row = new Object[]{
                    i + 1,
                    wr.getProject() != null ? wr.getProject().getName() : "",
                    wr.getTask() != null ? wr.getTask().getName() : "",
                    wr.getStartTime(),
                    wr.getEndTime(),
                    wr.getWorkMinutes(),
                    wr.getBreakWork(),
                    statusText,
                    wr.getRemarks()
            };

            model.addRow(row);
        }


        centerTableColumns(tblRecord);
    }
    private void centerTableColumns(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }


    private void loadUserInfo() {
        try {
            if (loggedInUser != null) {
                // Tên nhân viên
                txtEmployeeName.setText(loggedInUser.getUserName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadProjects() {
        try {
            ProjectDAO projectDAO = new ProjectDAO();
            List<Project> projects = projectDAO.findAll();

            cbProject.removeAllItems();
            for (Project p : projects) {
                cbProject.addItem(p.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try {
            TaskDAO taskDAO = new TaskDAO();
            List<Tasks> tasks = taskDAO.findAll();

            cbTask.removeAllItems();
            for (Tasks t : tasks) {
                cbTask.addItem(t.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        //  Ngày làm việc không được sau hôm nay
        Date today = new Date();
        Date selectedDate = csDate.getDate();

        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày làm việc!");
            return false;
        }

        if (selectedDate.after(today)) {
            JOptionPane.showMessageDialog(this, "Không được nhập ngày trong tương lai!");
            return false;
        }


        //  Check-in bắt buộc
        String checkInStr = fmEnd.getText().trim();
        if (checkInStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giờ Check-in!");
            return false;
        }

        // Parse check-in
        LocalTime checkIn;
        try {
            checkIn = LocalTime.parse(checkInStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giờ Check-in không hợp lệ!");
            return false;
        }

        // ==== 3. Check-out: cho phép bỏ trống ====
        String checkOutStr = fmCheckOut.getText().trim();
        LocalTime checkOut = null;

        if (!checkOutStr.isEmpty()) {
            try {
                checkOut = LocalTime.parse(checkOutStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Giờ Check-out không hợp lệ!");
                return false;
            }

            // Nếu có nhập checkout → phải sau checkin
            if (checkOut.isBefore(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-out phải sau Check-in!");
                return false;
            }
        }
        // ==== 4. Validate Holiday ====
        if (rbHoliday.isSelected()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String selectedStr = sdf.format(selectedDate);

            HolidayController holidayController = new HolidayController(new HolidayDAO());
            boolean isHoliday = holidayController.findAll()
                    .stream()
                    .anyMatch(h -> sdf.format(h.getDateHoliday()).equals(selectedStr));

            if (!isHoliday) {
                JOptionPane.showMessageDialog(this,
                        "Ngày này không phải ngày nghỉ! Không thể chọn Holiday.");
                rbHoliday.setSelected(false);
                return false;
            }
        }

        return true;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnWorkinformation = new javax.swing.JButton();
        lbEmployee = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        lbEarlieststarttime = new javax.swing.JLabel();
        fmEnd = fmCheckOut = new javax.swing.JFormattedTextField(
            new javax.swing.text.DateFormatter(
                new java.text.SimpleDateFormat("HH:mm")
            )
        );
        ;
        lbLatestendtime = new javax.swing.JLabel();
        fmCheckOut = fmCheckOut = new javax.swing.JFormattedTextField(
            new javax.swing.text.DateFormatter(
                new java.text.SimpleDateFormat("HH:mm")
            )
        );
        ;
        lbCalendar = new javax.swing.JLabel();
        csDate = new com.toedter.calendar.JDateChooser();
        rbHoliday = new javax.swing.JRadioButton();
        btnCreatecontent = new javax.swing.JButton();
        btnAddAttend = new javax.swing.JButton();
        lbStarttime = new javax.swing.JLabel();
        fmStart = fmEnd = new javax.swing.JFormattedTextField(
            new javax.swing.text.DateFormatter(
                new java.text.SimpleDateFormat("HH:mm")
            )
        );
        ;
        lbEndtime = new javax.swing.JLabel();
        lbBreaktimetotask = new javax.swing.JLabel();
        txtBreak = new javax.swing.JTextField();
        lbBreaktimetotask1 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        lbProject = new javax.swing.JLabel();
        cbProject = new javax.swing.JComboBox<>();
        lbTaskName = new javax.swing.JLabel();
        cbTask = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecord = new javax.swing.JTable();
        btnAddRecord = new javax.swing.JButton();
        btnUpdateRecord = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        fmCheckIn1 = fmCheckOut = new javax.swing.JFormattedTextField(
            new javax.swing.text.DateFormatter(
                new java.text.SimpleDateFormat("HH:mm")
            )
        );
        ;

        btnWorkinformation.setBackground(new java.awt.Color(255, 255, 204));
        btnWorkinformation.setText("勤怠情報");
        btnWorkinformation.addActionListener(this::btnWorkinformationActionPerformed);

        lbEmployee.setText("社員名");

        txtEmployeeName.setText("ログイン社員名");
        txtEmployeeName.addActionListener(this::txtEmployeeNameActionPerformed);

        lbEarlieststarttime.setText("最始");

        fmEnd.addActionListener(this::fmEndActionPerformed);

        lbLatestendtime.setText("最終");

        fmCheckOut.addActionListener(this::fmCheckOutActionPerformed);

        lbCalendar.setText("日付");

        rbHoliday.setText("休日");

        btnCreatecontent.setBackground(new java.awt.Color(255, 255, 204));
        btnCreatecontent.setText("作成内容");
        btnCreatecontent.setToolTipText("");
        btnCreatecontent.addActionListener(this::btnCreatecontentActionPerformed);

        btnAddAttend.setBackground(new java.awt.Color(204, 255, 204));
        btnAddAttend.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnAddAttend.setText("追加");
        btnAddAttend.addActionListener(this::btnAddAttendActionPerformed);

        lbStarttime.setText("開始");

        fmStart.addActionListener(this::fmStartActionPerformed);

        lbEndtime.setText("終了");

        lbBreaktimetotask.setText("休憩");

        lbBreaktimetotask1.setText("状態");

        lbProject.setText("プロジェクト名");

        lbTaskName.setText("タスク名");

        cbTask.addActionListener(this::cbTaskActionPerformed);

        jLabel1.setText("述べる");

        jToggleButton1.setBackground(new java.awt.Color(204, 204, 204));
        jToggleButton1.setText("合計");
        jToggleButton1.addActionListener(this::jToggleButton1ActionPerformed);

        tblRecord.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "プロジェクト名", "タスク名", "開始", "終了", "勤務時間", "休憩時間", "Status", "Remark"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblRecord.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(tblRecord);

        btnAddRecord.setBackground(new java.awt.Color(153, 255, 153));
        btnAddRecord.setText("追加");
        btnAddRecord.addActionListener(this::btnAddRecordActionPerformed);

        btnUpdateRecord.setBackground(new java.awt.Color(204, 255, 255));
        btnUpdateRecord.setText("編集");

        btnDelete.setBackground(new java.awt.Color(255, 204, 204));
        btnDelete.setText("削除");

        fmCheckIn1.addActionListener(this::fmCheckIn1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbStarttime, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fmStart, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(lbEndtime, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rbHoliday, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 166, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAddRecord)
                                .addGap(29, 29, 29)
                                .addComponent(btnUpdateRecord)
                                .addGap(36, 36, 36)
                                .addComponent(btnDelete)
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbBreaktimetotask, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtBreak, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                        .addComponent(lbBreaktimetotask1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(67, 67, 67))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbEarlieststarttime, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fmCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lbLatestendtime, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fmCheckIn1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAddAttend, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btnCreatecontent, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(fmEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lbProject)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbProject, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(lbTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbTask, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(39, 39, 39)
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(122, 122, 122))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(33, 33, 33)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(lbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(lbCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(38, 38, 38)
                                            .addComponent(csDate, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(btnWorkinformation, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(72, Short.MAX_VALUE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lbLatestendtime)
                .addGap(18, 18, 18)
                .addComponent(fmCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnAddAttend, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rbHoliday))
                        .addGap(16, 16, 16)
                        .addComponent(btnCreatecontent))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lbEarlieststarttime)
                        .addGap(18, 18, 18)
                        .addComponent(fmCheckIn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBreaktimetotask)
                    .addComponent(txtBreak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbBreaktimetotask1)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbEndtime)
                    .addComponent(fmStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbStarttime)
                    .addComponent(fmEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 474, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddRecord)
                    .addComponent(btnUpdateRecord)
                    .addComponent(btnDelete))
                .addGap(17, 17, 17))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addComponent(btnWorkinformation)
                    .addGap(7, 7, 7)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbEmployee)
                        .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(csDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbCalendar))
                    .addGap(144, 144, 144)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbProject)
                        .addComponent(cbProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbTaskName)
                        .addComponent(cbTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jToggleButton1)
                    .addGap(197, 197, 197)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(89, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void btnWorkinformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWorkinformationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnWorkinformationActionPerformed

    private void txtEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeeNameActionPerformed

    private void fmEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fmEndActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fmEndActionPerformed

    private void fmCheckOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fmCheckOutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fmCheckOutActionPerformed

    private void btnCreatecontentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreatecontentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCreatecontentActionPerformed

    private void btnAddAttendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAttendActionPerformed
        // TODO add your handling code here:
        if (!validateForm()) return;

        if (!validateForm()) return;

        Date selectedDate = csDate.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
            return;
        }

        String checkInStr = fmEnd.getText().trim();
        String checkOutStr = fmCheckOut.getText().trim();

        if (checkInStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giờ Check-in!");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            LocalDate workDate = LocalDate.parse(sdf.format(selectedDate));
            LocalTime checkIn = LocalTime.parse(checkInStr);
            LocalTime checkOut = null;
            if (!checkOutStr.isEmpty()) {
                checkOut = LocalTime.parse(checkOutStr);
            }

            // 1. Tìm Attendance
            Attendance attendance = attendanceController.findByUserAndDate(loggedInUser.getId(), workDate)
            .stream().findFirst().orElse(null);

            if (attendance == null) {
                // 2. Nếu chưa có -> tạo mới
                attendance = new Attendance();
                attendance.setUser(loggedInUser);
                attendance.setWorkDate(workDate);
                attendance.setCheckInTime(checkIn);
                attendance.setCheckOutTime(checkOut); // lưu luôn check-out nếu có
                attendance.setBreakMinutes(0);
                attendance.setHoliday(rbHoliday.isSelected());
                attendance.setStatus(Constant.ATTENDANCE_STATUS_PENDING);
                attendance.calculateTimes();
                attendance.setCreatedAt(LocalDateTime.now());
                attendance.setUpdatedAt(LocalDateTime.now());

                attendanceController.create(attendance);
            } else {
                // 3. Nếu đã có -> cập nhật
                if (attendance.getCheckInTime() == null) attendance.setCheckInTime(checkIn);
                if (checkOut != null) {
                    if (checkOut.isBefore(attendance.getCheckInTime())) {
                        JOptionPane.showMessageDialog(this, "Check-out không thể trước Check-in!");
                        return;
                    }
                    attendance.setCheckOutTime(checkOut);
                }

                // 4. Cập nhật breakMinutes từ WorkRecord
                int totalBreak = Math.toIntExact(recordController.sumBreakWorkByAttendanceId(attendance.getId()));
                attendance.setBreakMinutes(totalBreak);
                attendanceController.update(attendance);

                attendance.setBreakMinutes(totalBreak);

                attendance.calculateTimes();
                attendance.setUpdatedAt(LocalDateTime.now());

                attendanceController.update(attendance);
            }

            JOptionPane.showMessageDialog(this, "Attendance đã được cập nhật thành công!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnAddAttendActionPerformed

    private void fmStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fmStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fmStartActionPerformed

    private void cbTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTaskActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void btnAddRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecordActionPerformed
        // TODO add your handling code here:
        try {
            // 1. Lấy Attendance hiện tại
            Attendance attendance = attendanceController.findByUserAndDate(
                loggedInUser.getId(),
                LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd")
                    .format(csDate.getDate()))
            ).stream().reduce((first, second) -> second).orElse(null);

            if (attendance == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Attendance hiện tại!");
                return;
            }

            // 2. Lấy dữ liệu giờ bắt đầu / kết thúc
            String startStr = fmStart.getText().trim();
            String endStr = fmEnd.getText().trim();

            if (startStr.isEmpty() || endStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập giờ bắt đầu và kết thúc!");
                return;
            }

            LocalTime start = LocalTime.parse(startStr);
            LocalTime end = LocalTime.parse(endStr);

            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(this, "Giờ kết thúc phải sau giờ bắt đầu!");
                return;
            }

            // 3. Lấy Project và Task
            String projectName = (String) cbProject.getSelectedItem();
            String taskName = (String) cbTask.getSelectedItem();

            Project project = new ProjectDAO().findByName(projectName).orElse(null);
            Tasks task = new TaskDAO().findByName(taskName).orElse(null);

            // 4. Tính tổng phút block
            long totalMinutes = Duration.between(start, end).toMinutes();

            //  breakWork của block này (mặc định 0 nếu không nhập break riêng)
            int breakWork = 0;
            try {
                String breakStr = txtBreak.getText().trim();
                if (!breakStr.isEmpty()) {
                    breakWork = Integer.parseInt(breakStr);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Số phút nghỉ phải là số nguyên!");
                return;
            }

            // workMinutes = tổng phút - nghỉ
            int workMinutes = (int) totalMinutes - breakWork;
            if (workMinutes < 0) {
                JOptionPane.showMessageDialog(this, "Phút nghỉ không thể lớn hơn tổng thời gian block!");
                return;
            }

            // 5. Lấy status WorkRecord
            int status;
            switch (cbStatus.getSelectedIndex()) {
                case 0 -> status = Constant.WORK_RECORD_STATUS_PENDING;   // 未確認
                case 1 -> status = Constant.WORK_RECORD_STATUS_APPROVED;  // 確認済み
                default -> status = Constant.WORK_RECORD_STATUS_REJECTED; // 拒否済み
            }

            // 6. Tạo mới WorkRecord
            WorkRecord detail = new WorkRecord();
            detail.setAttendance(attendance);
            detail.setProject(project);
            detail.setTask(task);
            detail.setStartTime(start);
            detail.setEndTime(end);
            detail.setBreakWork(breakWork);
            detail.setWorkMinutes(workMinutes);
            detail.setRemarks(txtRemark.getText().trim());
            detail.setStatus(status);
            detail.setCreatedAt(LocalDateTime.now());
            detail.setUpdatedAt(LocalDateTime.now());

            // 7. Lưu WorkRecord
            recordController.createRecord(detail);

            // 8. Cập nhật lại tổng breakMinutes của attendance
            int totalBreak =
            attendance.getWorkRecords()
            .stream()
            .mapToInt(w -> w.getBreakWork() == null ? 0 : w.getBreakWork())
            .sum();

            attendance.setBreakMinutes(totalBreak);

            attendanceController.update(attendance);

            // 9. Refresh table
            JOptionPane.showMessageDialog(this, "Thêm Work Record thành công!");
            loadWorkRecordTable(attendance.getId());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnAddRecordActionPerformed

    private void fmCheckIn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fmCheckIn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fmCheckIn1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAttend;
    private javax.swing.JButton btnAddRecord;
    private javax.swing.JButton btnCreatecontent;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdateRecord;
    private javax.swing.JButton btnWorkinformation;
    private javax.swing.JComboBox<String> cbProject;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JComboBox<String> cbTask;
    private com.toedter.calendar.JDateChooser csDate;
    private javax.swing.JFormattedTextField fmCheckIn1;
    private javax.swing.JFormattedTextField fmCheckOut;
    private javax.swing.JFormattedTextField fmEnd;
    private javax.swing.JFormattedTextField fmStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lbBreaktimetotask;
    private javax.swing.JLabel lbBreaktimetotask1;
    private javax.swing.JLabel lbCalendar;
    private javax.swing.JLabel lbEarlieststarttime;
    private javax.swing.JLabel lbEmployee;
    private javax.swing.JLabel lbEndtime;
    private javax.swing.JLabel lbLatestendtime;
    private javax.swing.JLabel lbProject;
    private javax.swing.JLabel lbStarttime;
    private javax.swing.JLabel lbTaskName;
    private javax.swing.JRadioButton rbHoliday;
    private javax.swing.JTable tblRecord;
    private javax.swing.JTextField txtBreak;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtRemark;
    // End of variables declaration//GEN-END:variables
}
