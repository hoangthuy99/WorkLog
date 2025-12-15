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
import com.ra.View.dashboard.MainDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private Users attendanceUser;
    private AttendanceController attendanceController;
    private RecordController recordController;
    private TaskController taskController;
    private ProjectController projectController;
    private HolidayController holidayController;
    private  List<Attendance> a;  // Danh sách attendance của chính nhân viên đó
    private boolean viewMode = false;
    private Attendance currentAttendance;
    private List<WorkRecord> currentRecords;
    private boolean isLoadingRecord = false; // Thêm biến flag
    private LocalDate currentSelectedDate = null; // Thêm biến lưu ngày đang xem
    private Users displayUser;     // User của attendance (dùng để hiển thị)
    public AddAttendance(Users user,  List<Attendance> a,Users displayUser, boolean viewMode) {
        initComponents();
        this.loggedInUser = user;
        this.a = a;
        this.viewMode = viewMode;
        this.displayUser = displayUser;
        // Xác định attendanceUser
        if (a != null && !a.isEmpty()) {
            this.currentAttendance = a.get(0);
            // Nếu attendance có thông tin user, thì dùng user đó
            if (this.currentAttendance.getUser() != null) {
                this.attendanceUser = this.currentAttendance.getUser();
            } else {
                this.attendanceUser = this.displayUser; // Fallback: dùng displayUser
            }
        } else {
            this.attendanceUser = this.displayUser; // Nếu không có attendance, dùng displayUser
        }

        loadUserInfo();
        loadProjects();
        loadTasks();
        initDateChooserListener();
        initializeControllers();


        setupByRole();
        // Thêm sự kiện click cho bảng
        tblRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRecordMouseClicked(evt);
            }
        });

        // Load dữ liệu attendance
        if (currentAttendance != null) {
            loadAttendanceData(currentAttendance);
        }


        // Thiết lập chế độ xem/chỉnh sửa
        if (viewMode) {
            setupViewMode();
            canEdit();
        }
    }
    private boolean canEdit() {
        if (loggedInUser == null || attendanceUser == null) return false;

        int roleId = loggedInUser.getRole().getId();

        // ADMIN (3) hoặc MANAGER (2) luôn được sửa
        if (roleId == 2 || roleId == 3) return true;

        // EMPLOYEE (1) chỉ được sửa của chính mình
        return loggedInUser.getId() == attendanceUser.getId();
    }

    private void loadUserInfo() {
        Users u = attendanceUser != null ? attendanceUser : displayUser;

        if (u == null) return;
        txtEmployeeName.setText(u.getFullName());
    }

    private void tblRecordMouseClicked(java.awt.event.MouseEvent evt) {
        if (viewMode) return;
        if (evt.getClickCount() == 1) { // Single click
            int selectedRow = tblRecord.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    // Lấy ID từ cột ẩn (giả sử cột 0 là ID)
                    int recordId = (int) tblRecord.getValueAt(selectedRow, 0);

                    // Lấy dữ liệu từ database
                    List<WorkRecord> records = recordController.findById(recordId);
                    if (records != null && !records.isEmpty()) {
                        WorkRecord record = records.get(0);

                        // Hiển thị dữ liệu lên form
                        loadRecordToForm(record);

                        // Enable nút Update, disable nút Add
                        btnUpdateRecord.setEnabled(true);
                        btnAddRecord.setEnabled(false);
                        btnDelete.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "データの読み込み中にエラーが発生しました: " + e.getMessage(),
                            "エラー",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private void loadRecordToForm(WorkRecord record) {
        if (record == null) return;

        try {
            // Đánh dấu đang load record (để ngăn sự kiện date change)
            isLoadingRecord = true;

            // Hiển thị thông tin cơ bản
            if (record.getStartTime() != null) {
                fmStart.setText(formatTime(record.getStartTime()));
            }
            if (record.getEndTime() != null) {
                fmEnd.setText(formatTime(record.getEndTime()));
            }

            txtBreak.setText(record.getBreakWork() != null ?
                    String.valueOf(record.getBreakWork()) : "0");
            txtRemark.setText(record.getRemarks() != null ? record.getRemarks() : "");

            // Hiển thị project và task
            if (record.getProject() != null && record.getProject().getName() != null) {
                cbProject.setSelectedItem(record.getProject().getName());
            }
            if (record.getTask() != null && record.getTask().getName() != null) {
                cbTask.setSelectedItem(record.getTask().getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Reset flag sau khi load xong
            isLoadingRecord = false;
        }
    }

    private String formatTime(LocalTime time) {
        if (time == null) return "";
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    private boolean isManager(Users user) {
        if (user == null || user.getRole() == null) return false;

        int roleId = user.getRole().getId();
        // 1 = EMPLOYEE, 2 = MANAGER, 3 = ADMIN
        return roleId == 3|| roleId==2;
    }

    private void loadAttendanceData(Attendance att) {
        if (att == null) return;

        try {
            //  Ngày làm việc
            if (att.getWorkDate() != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(att.getWorkDate());
                java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
                csDate.setDate(utilDate);
            }
            //  Ngày nghỉ
            rbHoliday.setSelected(att.isHoliday());


            // Thời gian
            if (att.getCheckInTime() != null) {
                fmCheckIn.setText(att.getCheckInTime().toString());
            }

            if (att.getCheckOutTime() != null) {
                fmCheckOut.setText(att.getCheckOutTime().toString());
            }

            //  Load work record table
            loadWorkRecordTable(att.getId());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "データの読み込み中にエラーが発生しました。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public AddAttendance(Users user, List<Attendance> a, boolean viewMode) {
        this(user, a, user, false);
    }


    private void initializeControllers() {
        this.attendanceController = new AttendanceController();
        this.recordController = new RecordController();
        this.taskController = new TaskController();
        this.projectController = new ProjectController();
        this.holidayController = new HolidayController(new HolidayDAO());
    }
    private void setupByRole() {
        // Employee (không phải Manager/Admin) thì không cho đổi Status
        if (!isManager(loggedInUser)) {
             // 未確認 (PENDING)
        }
    }

    private void setupViewMode() {
        //  Khóa toàn bộ ô nhập
        cbProject.setEnabled(false);
        cbTask.setEnabled(false);
        txtRemark.setEnabled(false);
        csDate.setEnabled(false);
        fmStart.setEnabled(false);
        fmEnd.setEnabled(false);
        fmCheckIn.setEnabled(false);
        fmCheckOut.setEnabled(false);
        txtBreak.setEnabled(false);
        rbHoliday.setEnabled(false);
        txtEmployeeName.setEnabled(false); //fix sáng 13/12

        //  Khóa bảng & nút thao tác
        tblRecord.setEnabled(false);
        btnAddAttend.setVisible(false);
        btnAddRecord.setVisible(false);
        btnUpdateRecord.setVisible(false);
        btnDelete.setVisible(false);
    }

    private List<Attendance> getCurrentAttendance(int userId, LocalDate date) {
        return attendanceController.findByUserAndDate(userId, date);
    }

    private void loadWorkRecordTable(int attendanceId) {
        List<WorkRecord> list = recordController.findByAttendanceId(Math.toIntExact(attendanceId));
        this.currentRecords = list;

        DefaultTableModel model = (DefaultTableModel) tblRecord.getModel();
        model.setRowCount(0); // clear table

        for (int i = 0; i < list.size(); i++) {
            WorkRecord wr = list.get(i);

            Object[] row = new Object[]{
                    wr.getId(),      // 0: ID (ẩn)
                    i + 1,           // 1: No
                    wr.getProject() != null ? wr.getProject().getName() : "",
                    wr.getTask() != null ? wr.getTask().getName() : "",
                    wr.getStartTime(),              // 4: 開始
                    wr.getEndTime(),                // 5: 終了
                    wr.getWorkTimeFormatted(),      // 6: 勤務時間
                    wr.getBreakWork(),              // 7: 休憩時間
                    wr.getRemarks()                 // 8: ノート   đúng chỗ
            };

            model.addRow(row);
        }

        // Ẩn cột ID (cột 0)
        tblRecord.getColumnModel().getColumn(0).setMinWidth(0);
        tblRecord.getColumnModel().getColumn(0).setMaxWidth(0);
        tblRecord.getColumnModel().getColumn(0).setWidth(0);
        tblRecord.getColumnModel().getColumn(0).setPreferredWidth(0);

        centerTableColumns(tblRecord);
    }



    private void centerTableColumns(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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
            JOptionPane.showMessageDialog(this, "勤務日を選択してください！");
            return false;
        }

        if (selectedDate.after(today)) {
            JOptionPane.showMessageDialog(this, "未来の日付は入力できません！");
            return false;
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
                        "この日は休暇日ではありません！Holiday を選択できません。");
                rbHoliday.setSelected(false);
                return false;
            }
        }

        return true;
    }
    // Thêm sự kiện cho DateChooser
    private void initDateChooserListener() {
        csDate.addPropertyChangeListener("date", evt -> {
            if (evt.getNewValue() != null) {
                loadAttendanceForSelectedDate();
            }
        });
    }



    private long calculateMinutesAllowOver24(String startStr, String endStr) {
        String[] s = startStr.split(":");
        String[] e = endStr.split(":");

        int startH = Integer.parseInt(s[0].trim());
        int startM = Integer.parseInt(s[1].trim());

        int endH = Integer.parseInt(e[0].trim());
        int endM = Integer.parseInt(e[1].trim());

        // Làm tròn phút xuống bội số 10
        startM = roundToNearest10(startM);
        endM   = roundToNearest10(endM);

        // Nếu làm tròn ra 60 phút → +1 giờ, phút = 0
        if (startM == 60) {
            startM = 0;
            startH += 1;
        }
        if (endM == 60) {
            endM = 0;
            endH += 1;
        }

        // Chuyển thành phút
        int startTotal = startH * 60 + startM;
        int endTotal   = endH * 60 + endM;

        // ⚠ Nếu end < start → hiểu là qua ngày → +24h
        if (endTotal < startTotal) {
            endTotal += 24 * 60;
        }

        return endTotal - startTotal;
    }


    private void loadAttendanceForSelectedDate() {
        try {
            Date selectedDate = csDate.getDate();
            if (selectedDate == null) return;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LocalDate workDate = LocalDate.parse(sdf.format(selectedDate));

            // Tìm attendance hiện có
            List<Attendance> attendances = attendanceController.findByUserAndDate(
                    loggedInUser.getId(), workDate);

            if (!attendances.isEmpty()) {
                Attendance attendance = attendances.get(0);

                // Fill dữ liệu vào form
                if (attendance.getCheckInTime() != null) {
                    fmCheckIn.setText(attendance.getCheckInTime().toString());
                }
                if (attendance.getCheckOutTime() != null) {
                    fmCheckOut.setText(attendance.getCheckOutTime().toString());
                }

                rbHoliday.setSelected(attendance.isHoliday());

                // Load work records
                loadWorkRecordTable(attendance.getId());

                System.out.println("既存出勤データを読み込みました： " + workDate);
            } else {
                // Clear form nếu không có attendance
                clearAttendanceForm();
                System.out.println("該当日の出勤データはありません：" + workDate);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearAttendanceForm() {
        fmCheckIn.setText("");
        fmCheckOut.setText("");
        rbHoliday.setSelected(false);
        // Clear table
        DefaultTableModel model = (DefaultTableModel) tblRecord.getModel();
        model.setRowCount(0);
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
        fmEnd = fmEnd = new javax.swing.JFormattedTextField(
                new javax.swing.text.DateFormatter(
                        new java.text.SimpleDateFormat("HH:mm")
                )
        );
        ;
        lbLatestendtime = new javax.swing.JLabel();
        fmCheckOut = new javax.swing.JFormattedTextField();
        lbCalendar = new javax.swing.JLabel();
        csDate = new com.toedter.calendar.JDateChooser();
        rbHoliday = new javax.swing.JRadioButton();
        btnCreatecontent = new javax.swing.JButton();
        btnAddAttend = new javax.swing.JButton();
        lbStarttime = new javax.swing.JLabel();
        fmStart = fmStart = new javax.swing.JFormattedTextField(
                new javax.swing.text.DateFormatter(
                        new java.text.SimpleDateFormat("HH:mm")
                )
        );
        ;
        lbEndtime = new javax.swing.JLabel();
        lbBreaktimetotask = new javax.swing.JLabel();
        txtBreak = new javax.swing.JTextField();
        lbBreaktimetotask1 = new javax.swing.JLabel();
        lbProject = new javax.swing.JLabel();
        cbProject = new javax.swing.JComboBox<>();
        lbTaskName = new javax.swing.JLabel();
        cbTask = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecord = new javax.swing.JTable();
        btnAddRecord = new javax.swing.JButton();
        btnUpdateRecord = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        fmCheckIn = fmCheckIn = new javax.swing.JFormattedTextField(
            new javax.swing.text.DateFormatter(
                new java.text.SimpleDateFormat("HH:mm")
            )
        );
        ;
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        btnBack = new javax.swing.JButton();
        btnBack.setBackground(new java.awt.Color(204, 204, 255));
        btnBack.setText("戻る");
        btnBack.addActionListener(this::btnBackActionPerformed);




        btnWorkinformation.setBackground(new java.awt.Color(255, 255, 204));
        btnWorkinformation.setText("勤怠情報");

        lbEmployee.setText("社員名");

        txtEmployeeName.setText("");

        lbEarlieststarttime.setText("最始");

        lbLatestendtime.setText("最終");

        lbCalendar.setText("日付");

        rbHoliday.setText("休日");

        btnCreatecontent.setBackground(new java.awt.Color(255, 255, 204));
        btnCreatecontent.setText("作成内容");
        btnCreatecontent.setToolTipText("");

        btnAddAttend.setBackground(new java.awt.Color(204, 255, 204));
        btnAddAttend.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnAddAttend.setText("追加");
        btnAddAttend.addActionListener(this::btnAddAttendActionPerformed);

        lbStarttime.setText("開始");

        fmStart.addActionListener(this::fmStartActionPerformed);

        lbEndtime.setText("終了");

        lbBreaktimetotask.setText("休憩");

        lbBreaktimetotask1.setText("");

        lbProject.setText("プロジェクト名");

        lbTaskName.setText("タスク名");

        cbTask.addActionListener(this::cbTaskActionPerformed);

        jLabel1.setText("ノート");

        tblRecord.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "ID", "No", "プロジェクト名", "タスク名",
                        "開始", "終了", "勤務時間", "休憩時間", "ノート"
                }
        ) {
            Class[] types = new Class [] {
                    Integer.class, // ID
                    Integer.class, // No
                    String.class,  // Project
                    String.class,  // Task
                    String.class,  // Start
                    String.class,  // End
                    String.class,  // Work time
                    String.class,  // Break
                    String.class   // Note
            };

            boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false
            };

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        if (tblRecord.getColumnCount() > 0) {
            // Ẩn cột ID (cột 0)
            tblRecord.getColumnModel().getColumn(0).setMinWidth(0);
            tblRecord.getColumnModel().getColumn(0).setMaxWidth(0);
            tblRecord.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblRecord.getColumnModel().getColumn(0).setWidth(0);

            // Set resizable cho các cột còn lại
            for (int i = 1; i < tblRecord.getColumnCount(); i++) {
                tblRecord.getColumnModel().getColumn(i).setResizable(false);
            }
        }
        tblRecord.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(tblRecord);
        tblRecord.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblRecord.getColumnModel().getColumnCount() > 0) {
            tblRecord.getColumnModel().getColumn(0).setResizable(false);
            tblRecord.getColumnModel().getColumn(1).setResizable(false);
            tblRecord.getColumnModel().getColumn(2).setResizable(false);
            tblRecord.getColumnModel().getColumn(3).setResizable(false);
            tblRecord.getColumnModel().getColumn(4).setResizable(false);
            tblRecord.getColumnModel().getColumn(5).setResizable(false);
            tblRecord.getColumnModel().getColumn(6).setResizable(false);
            tblRecord.getColumnModel().getColumn(7).setResizable(false);
            tblRecord.getColumnModel().getColumn(8).setResizable(false);
        }

        btnAddRecord.setBackground(new java.awt.Color(153, 255, 153));
        btnAddRecord.setText("追加");
        btnAddRecord.addActionListener(this::btnAddRecordActionPerformed);

        btnUpdateRecord.setBackground(new java.awt.Color(204, 255, 255));
        btnUpdateRecord.setText("編集");
        btnUpdateRecord.addActionListener(this::btnUpdateRecordActionPerformed);

        btnDelete.setBackground(new java.awt.Color(255, 204, 204));
        btnDelete.setText("削除");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 3, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("HH:mm");

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 3, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("HH:mm");

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 3, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("HH:mm");

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 3, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("HH:mm");

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 3, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("mm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCreatecontent, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(45, 45, 45)
                                        .addComponent(lbStarttime, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lbProject))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fmStart, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                                                    // btnBack bên trái
                                                    .addComponent(btnBack)

                                                    // khoảng trống co giãn để đẩy 3 nút sang phải
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, Short.MAX_VALUE, Short.MAX_VALUE)
                                                    .addComponent(btnAddRecord)
                                                .addGap(38, 38, 38)
                                                .addComponent(btnUpdateRecord)
                                                .addGap(34, 34, 34)
                                                .addComponent(btnDelete)
                                                .addGap(149, 149, 149))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addComponent(lbEndtime, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(6, 6, 6)
                                                        .addComponent(jLabel4)
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(fmEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(34, 34, 34)
                                                        .addComponent(lbBreaktimetotask, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(txtBreak, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jLabel6)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lbBreaktimetotask1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGap(87, 87, 87))))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cbProject, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(31, 31, 31)
                                                .addComponent(lbTaskName, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbTask, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(61, 61, 61)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap())))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(lbCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(csDate, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbHoliday, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(284, 284, 284)
                        .addComponent(lbEarlieststarttime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(fmCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(101, 101, 101)
                                .addComponent(lbLatestendtime, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(btnAddAttend, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fmCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(124, 124, 124))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(lbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnWorkinformation, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(55, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))

        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fmCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbLatestendtime))
                        .addComponent(rbHoliday))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fmCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbEarlieststarttime))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(csDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lbCalendar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(36, 36, 36)
                        .addComponent(btnAddAttend, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCreatecontent)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBreaktimetotask)
                    .addComponent(txtBreak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbBreaktimetotask1)
                    .addComponent(lbEndtime)
                    .addComponent(fmStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbStarttime)
                    .addComponent(fmEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbProject)
                    .addComponent(lbTaskName)
                    .addComponent(cbTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddRecord)
                    .addComponent(btnUpdateRecord)
                    .addComponent(btnDelete))
                .addGap(22, 22, 22))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addComponent(btnWorkinformation)
                    .addGap(7, 7, 7)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbEmployee)
                        .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(289, 289, 289)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(69, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(323, 323, 323)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(332, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // thêm nút goback
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        MainDashboard md = findParentMainDashboard(this);
        if (md != null) {
            md.goBack();
        } else {
            // fallback nếu không tìm thấy dashboard
            JOptionPane.showMessageDialog(this, "メイン画面が見つかりません。");
        }
    }

    private MainDashboard findParentMainDashboard(java.awt.Component component) {
        java.awt.Container parent = component.getParent();
        while (parent != null) {
            if (parent instanceof MainDashboard) {
                return (MainDashboard) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }



    // Làm tròn phút xuống bội số 10 (0,10,20,30,40,50)
    private int roundToNearest10(int minute) {
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("分は 0〜59 の範囲で入力してください");
        }
        return Math.round(minute / 10f) * 10;
    }


    public LocalTime parseFlexibleTime(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] parts = input.split(":");
        if (parts.length < 2) {
            throw new IllegalArgumentException("時間の形式が無効です： " + input);
        }

        int hour = Integer.parseInt(parts[0].trim());
        int minute = Integer.parseInt(parts[1].trim());

        // Cho phép nhập từ 0 đến 30 giờ (giờ *gốc* người dùng nhập)
        if (hour > 30 || hour < 0) {
            throw new IllegalArgumentException("時間は 0〜30 の範囲で入力してください");
        }
        if (minute > 59 || minute < 0) {
            throw new IllegalArgumentException("分は 0〜59 の範囲で入力してください");
        }

        // 🔹 Làm tròn phút
        minute = roundToNearest10(minute);   // 0..60

        // Nếu làm tròn ra 60 phút → tăng 1 giờ, phút = 0
        if (minute == 60) {
            minute = 0;
            hour += 1;
        }

        // Sau khi cộng giờ, vẫn giới hạn tối đa 30h
        if (hour > 30) {
            throw new IllegalArgumentException("時間は 0〜30 の範囲で入力してください");
        }

        // Nếu giờ >= 24, chuyển về giờ trong ngày (0-23)
        return LocalTime.of(hour % 24, minute);
    }


    // Tính số ngày thêm nếu giờ >= 24
    public int getExtraDay(String input) {
        if (input == null || input.trim().isEmpty()) {
            return 0;
        }

        String[] parts = input.split(":");
        if (parts.length < 1) {
            return 0;
        }

        int hour = Integer.parseInt(parts[0].trim());
        // Nếu giờ >= 24 → thêm 1 ngày
        // Nếu giờ >= 48 → thêm 2 ngày (theo yêu cầu tối đa 30h)
        return hour / 24;
    }

    // Phương thức mới: chuyển đổi thời gian nhập vào thành LocalDateTime
    public LocalDateTime parseToDateTime(LocalDate baseDate, String timeInput) {
        if (timeInput == null || timeInput.trim().isEmpty()) {
            return null;
        }

        LocalTime time = parseFlexibleTime(timeInput);
        int extraDays = getExtraDay(timeInput);

        return LocalDateTime.of(baseDate, time).plusDays(extraDays);
    }
    private boolean calculateAttendanceTimes(Attendance attendance,
                                             LocalDateTime checkInDT,
                                             LocalDateTime checkOutDT) {
        // Nếu chưa có check-out -> chưa tính gì nhưng vẫn OK
        if (checkOutDT == null) {
            attendance.setTotalMinutes(0);
            attendance.setOvertimeMinutes(0);
            return true;
        }

        // Nếu là ngày thường (không phải holiday) thì bắt buộc phải có WorkRecord
        if (!attendance.isHoliday()) {
            List<WorkRecord> records =
                    recordController.findByAttendanceId(attendance.getId());

            if (records == null || records.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "通常勤務の日は、少なくとも1件の業務記録（WorkRecord）が必要です。\n" +
                                "先に業務内容を登録してください。");
                return false;   //  KHÔNG HỢP LỆ
            }
        }

        if (checkInDT == null) {
            attendance.setTotalMinutes(0);
            attendance.setOvertimeMinutes(0);
            return false;
        }

        try {
            long totalMinutes = Duration.between(checkInDT, checkOutDT).toMinutes();

            // Trừ break
            totalMinutes -= attendance.getBreakMinutes();
            if (totalMinutes < 0) totalMinutes = 0;

            attendance.setTotalMinutes((int) totalMinutes);

            // Tính overtime (giả sử làm việc tiêu chuẩn 8 tiếng = 480 phút)
            int standardWorkMinutes = 8 * 60; // 480 phút
            int overtime = (int) totalMinutes - standardWorkMinutes;
            attendance.setOvertimeMinutes(Math.max(overtime, 0));

            return true;  //  OK

        } catch (Exception e) {
            System.err.println("出勤時間計算エラー： " + e.getMessage());
            attendance.setTotalMinutes(0);
            attendance.setOvertimeMinutes(0);
            return false; //  lỗi
        }
    }



    private void btnAddAttendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAttendActionPerformed

        if (!validateForm()) return;

        Date selectedDate = csDate.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "日付を選択してください！");
            return;
        }

        String checkInStr = fmCheckIn.getText().trim();
        String checkOutStr = fmCheckOut.getText().trim();

        if (checkInStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "チェックイン時間を入力してください！");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            LocalDate workDate = LocalDate.parse(sdf.format(selectedDate));

            // Xử lý check-in (chỉ cho phép 0-23h)
            // Xử lý check-in (chỉ cho phép 0-23h, làm tròn phút 10)
            LocalTime checkIn;
            try {
                String[] parts = checkInStr.split(":");
                if (parts.length < 2) {
                    JOptionPane.showMessageDialog(this,
                            "チェックイン時間が無効です！00:00〜23:59 の形式で入力してください。");
                    return;
                }

                int hour = Integer.parseInt(parts[0].trim());
                int minute = Integer.parseInt(parts[1].trim());

                if (hour < 0 || hour > 23) {
                    JOptionPane.showMessageDialog(this,
                            "チェックイン時間は 00:00〜23:59 の範囲で入力してください！");
                    return;
                }
                if (minute < 0 || minute > 59) {
                    JOptionPane.showMessageDialog(this,
                            "分は 0〜59 の範囲で入力してください！");
                    return;
                }

                //  Làm tròn phút tới bội số 10 (0..60)
                minute = roundToNearest10(minute);

                // Nếu phút = 60 → +1 giờ, phút = 0
                if (minute == 60) {
                    minute = 0;
                    hour += 1;
                }

                // Sau khi cộng, vẫn phải trong 0–23
                if (hour < 0 || hour > 23) {
                    JOptionPane.showMessageDialog(this,
                            "チェックイン時間は 00:00〜23:59 の範囲で入力してください！");
                    return;
                }

                checkIn = LocalTime.of(hour, minute);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "チェックイン時間が無効です！00:00〜23:59 の範囲で入力してください。");
                return;
            }



            LocalTime checkOut = null;
            int extraDay = 0;

            // Xử lý check-out (cho phép 0-30h, làm tròn phút 10)
            LocalDateTime checkOutDT = null;
            if (!checkOutStr.isEmpty()) {
                try {
                    // Validate định dạng check-out
                    String[] parts = checkOutStr.split(":");
                    if (parts.length < 2) {
                        JOptionPane.showMessageDialog(this,
                                "チェックアウト時間が無効です！00:00〜29:59 の形式で入力してください。");
                        return;
                    }

                    int hour = Integer.parseInt(parts[0].trim());
                    int minute = Integer.parseInt(parts[1].trim());

                    // Giới hạn input gốc: 0〜30h
                    if (hour < 0 || hour > 30) {
                        JOptionPane.showMessageDialog(this,
                                "チェックアウト時間は 00:00〜29:59 の範囲で入力してください！");
                        return;
                    }
                    if (minute < 0 || minute > 59) {
                        JOptionPane.showMessageDialog(this,
                                "分は 0〜59 の範囲で入力してください！");
                        return;
                    }

                    // 🔹 Làm tròn phút tới bội số 10 (0..60)
                    minute = roundToNearest10(minute);

                    // Nếu phút = 60 → +1 giờ, phút = 0
                    if (minute == 60) {
                        minute = 0;
                        hour += 1;
                    }

                    // Sau khi cộng thêm giờ vì làm tròn,
                    // vẫn không được vượt quá 30h (theo rule của check-out)
                    if (hour < 0 || hour > 30) {
                        JOptionPane.showMessageDialog(this,
                                "チェックアウト時間は 00:00〜29:59 の範囲で入力してください！");
                        return;
                    }

                    // Nếu giờ >= 24 → thêm ngày và chuyển về giờ trong ngày
                    if (hour >= 24) {
                        extraDay = hour / 24;   // 24〜30 → extraDay = 1
                        hour = hour % 24;       // 24→0, 25→1, ...
                    }

                    checkOut = LocalTime.of(hour, minute);
                    checkOutDT = LocalDateTime.of(workDate, checkOut).plusDays(extraDay);

                    checkOutDT = LocalDateTime.of(workDate, checkOut).plusDays(extraDay);

                    System.out.println("DEBUG: Check-out parsed - Input: " + checkOutStr +
                            ", Time: " + checkOut +
                            ", Extra days: " + extraDay +
                            ", Final datetime: " + checkOutDT);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "チェックアウト時間が無効です： " + e.getMessage());
                    return;
                }
            }



            // QUAN TRỌNG: Validate check-out không thể trước check-in (đã tính extra day)
            LocalDateTime checkInDT = LocalDateTime.of(workDate, checkIn);
            if (checkOutDT != null) {
                if (checkOutDT.isBefore(checkInDT)) {
                    JOptionPane.showMessageDialog(this,
                            String.format("チェックアウト (%s) はチェックイン (%s) より前にはできません！",
                                    checkOutDT, checkInDT));
                    return;
                }

                // DEBUG: In ra thông tin để kiểm tra
                System.out.println("DEBUG: Check-in datetime: " + checkInDT);
                System.out.println("DEBUG: Check-out datetime: " + checkOutDT);
                System.out.println("DEBUG: Duration between: " +
                        Duration.between(checkInDT, checkOutDT).toMinutes() + " minutes");
            }

            // 2. Tìm attendance hiện có
            List<Attendance> attendances = attendanceController.findByUserAndDate(
                    loggedInUser.getId(), workDate);

            Attendance attendance = null;
            if (attendances != null && !attendances.isEmpty()) {
                attendance = attendances.get(0);
                System.out.println("DEBUG: Found existing attendance ID: " + attendance.getId());
            }

            if (attendance == null) {
                // THÊM MỚI
                System.out.println("DEBUG: Creating NEW attendance");
                attendance = new Attendance();
                attendance.setUser(loggedInUser);
                attendance.setWorkDate(workDate);
                attendance.setBreakMinutes(0);
                attendance.setHoliday(rbHoliday.isSelected());
                attendance.setStatus(Constant.ATTENDANCE_STATUS_PENDING);

                //  Validate / tính toán trước khi set checkOut vào entity
                boolean ok = calculateAttendanceTimes(attendance, checkInDT, checkOutDT);
                if (!ok) {
                    // Ví dụ: cố checkout nhưng chưa có WorkRecord
                    // => KHÔNG tạo attendance, KHÔNG lưu checkOut
                    return;
                }

                // Nếu OK thì mới set giờ vào
                attendance.setCheckInTime(checkIn);
                attendance.setCheckOutTime(checkOut);
                attendance.setExtraDay(extraDay);

                attendance.setCreatedAt(LocalDateTime.now());
                attendance.setUpdatedAt(LocalDateTime.now());

                attendanceController.create(attendance);
                JOptionPane.showMessageDialog(this, "出勤データが正常に追加されました！");
            } else {
                // CẬP NHẬT
                System.out.println("DEBUG: UPDATING existing attendance ID: " + attendance.getId());

                int newExtraDay = 0;
                LocalTime newCheckOut = null;
                LocalDateTime newCheckOutDT = null;

                if (!checkOutStr.isEmpty()) {
                    String[] parts = checkOutStr.split(":");
                    int hour = Integer.parseInt(parts[0].trim());
                    int minute = Integer.parseInt(parts[1].trim());

                    if (hour >= 24) {
                        newExtraDay = hour / 24;
                        hour = hour % 24;
                    }

                    newCheckOut = LocalTime.of(hour, minute);
                    newCheckOutDT = LocalDateTime.of(workDate, newCheckOut).plusDays(newExtraDay);
                }

                // Tính break từ work records
                int totalBreak = 0;
                try {
                    totalBreak = recordController.sumBreakWorkByAttendanceId(attendance.getId());
                    System.out.println("DEBUG: Total break minutes = " + totalBreak);
                } catch (Exception e) {
                    System.err.println("休憩時間を計算できませんでした: " + e.getMessage());
                    totalBreak = 0;
                }
                attendance.setBreakMinutes(totalBreak);

                //  Validate / tính toán trước khi set checkOut vào entity
                boolean ok = calculateAttendanceTimes(attendance, checkInDT, newCheckOutDT);
                if (!ok) {
                    // Ví dụ: chưa có WorkRecord mà muốn checkout
                    // => KHÔNG lưu checkOut, KHÔNG update DB
                    return;
                }

                //  OK -> mới set và lưu DB
                attendance.setCheckInTime(checkIn);
                attendance.setCheckOutTime(newCheckOut);
                attendance.setExtraDay(newExtraDay);
                attendance.setUpdatedAt(LocalDateTime.now());

                boolean updateSuccess = attendanceController.update(attendance);
                if (updateSuccess) {
                    JOptionPane.showMessageDialog(this, "出勤データが正常に更新されました！");
                } else {
                    JOptionPane.showMessageDialog(this, "出勤データの更新に失敗しました！");
                    return;
                }
            }

            // Load lại dữ liệu work records
            System.out.println("DEBUG: Attendance is null after create/update!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "エラー： " + ex.getMessage());
        }
    }//GEN-LAST:event_btnAddAttendActionPerformed

    private void fmStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fmStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fmStartActionPerformed

    private void cbTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTaskActionPerformed

    private void btnAddRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecordActionPerformed
        try {
            // 1. Lấy ngày
            if (csDate.getDate() == null) {
                JOptionPane.showMessageDialog(this, "日付を選択してください！");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LocalDate workDate = LocalDate.parse(sdf.format(csDate.getDate()));

            // 2. Tìm Attendance
            Attendance attendance = attendanceController.findByUserAndDate(
                    loggedInUser.getId(), workDate
            ).stream().findFirst().orElse(null);

            // 2b. Nếu chưa có → tạo mới
            if (attendance == null) {
                attendance = new Attendance();
                attendance.setUser(loggedInUser);
                attendance.setWorkDate(workDate);
                attendance.setBreakMinutes(0);
                attendance.setHoliday(rbHoliday.isSelected());
                attendance.setStatus(Constant.ATTENDANCE_STATUS_PENDING);
                attendanceController.create(attendance);
            }

            //  2c. Lấy tất cả WorkRecord hiện có của attendance này
            List<WorkRecord> existingRecords = recordController.findByAttendanceId(attendance.getId());

            //  GIỚI HẠN TỐI ĐA 20 RECORD / 1 NGÀY
            if (existingRecords != null && existingRecords.size() >= 20) {
                JOptionPane.showMessageDialog(this, "1日あたりの勤怠記録は最大20件までです。");
                return;
            }

            // 3. Lấy giờ bắt đầu + kết thúc (CHO PHÉP 0〜30h)
            String startStr = fmStart.getText().trim();
            String endStr   = fmEnd.getText().trim();

            if (startStr.isEmpty() || endStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "開始時間と終了時間を入力してください！");
                return;
            }

            // Parse sang LocalTime (cho phép 0〜30h, làm tròn phút)
            LocalTime newStart;
            LocalTime newEnd;
            try {
                newStart = parseFlexibleTime(startStr);
                newEnd   = parseFlexibleTime(endStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "時間の形式が無効です。");
                return;
            }

            // ⚡ Tính tổng phút theo 0〜30h (cho phép qua 24h)
            long totalMinutes;
            try {
                totalMinutes = calculateMinutesAllowOver24(startStr, endStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "時間の形式が無効です。");
                return;
            }

            if (totalMinutes <= 0) {
                JOptionPane.showMessageDialog(this, "終了時間は開始時間より後でなければなりません！");
                return;
            }

            //  BẮT BUỘC CHECK-IN TRƯỚC KHI THÊM WORKRECORD
            if (attendance.getCheckInTime() == null) {
                JOptionPane.showMessageDialog(this,
                        "先に出勤時間（チェックイン）を登録してください。");
                return;
            }

            //  NẾU ĐÃ CHECK-OUT THÌ NHÂN VIÊN KHÔNG ĐƯỢC THÊM RECORD NỮA
            if (attendance.getCheckOutTime() != null && !isManager(loggedInUser)) {
                JOptionPane.showMessageDialog(this,
                        "チェックアウト済みのため、勤怠記録を追加できません！");
                return;
            }

            // 3b. CHECK TRÙNG GIỜ VỚI CÁC WORKRECORD ĐÃ CÓ (dùng LocalTime đã parse)
            if (existingRecords != null) {

                int newStartMin = newStart.getHour() * 60 + newStart.getMinute();
                int newEndMin   = newEnd.getHour() * 60 + newEnd.getMinute();

                for (WorkRecord wr : existingRecords) {
                    LocalTime eStart = wr.getStartTime();
                    LocalTime eEnd   = wr.getEndTime();
                    if (eStart == null || eEnd == null) continue;

                    int eStartMin = eStart.getHour() * 60 + eStart.getMinute();
                    int eEndMin   = eEnd.getHour() * 60 + eEnd.getMinute();

                    // Điều kiện overlap: [newStart, newEnd) giao với [eStart, eEnd)
                    if (newStartMin < eEndMin && eStartMin < newEndMin) {
                        JOptionPane.showMessageDialog(this,
                                "この時間帯は既存の勤怠記録と重複しています。\n" +
                                        "既存: " + eStart + "〜" + eEnd);
                        return;
                    }
                }
            }

            // 4. Lấy project + task
            Project project = projectController.findByName((String) cbProject.getSelectedItem()).orElse(null);
            Tasks task     = taskController.findByName((String) cbTask.getSelectedItem()).orElse(null);

            if (project == null || task == null) {
                JOptionPane.showMessageDialog(this, "プロジェクトまたはタスクが見つかりません！");
                return;
            }

            // 5. Break time
            int breakWork = 0;
            if (!txtBreak.getText().trim().isEmpty()) {
                breakWork = Integer.parseInt(txtBreak.getText().trim());
            }

            int workMinutes = (int) totalMinutes - breakWork;
            if (workMinutes < 0) {
                JOptionPane.showMessageDialog(this, "休憩時間は総勤務時間を超えることはできません！");
                return;
            }

            // 7. Tạo WorkRecord
            WorkRecord record = new WorkRecord();
            record.setAttendance(attendance);
            record.setProject(project);
            record.setTask(task);
            record.setStartTime(newStart);   // LocalTime (0〜23, đã làm tròn)
            record.setEndTime(newEnd);
            record.setBreakWork(breakWork);
            record.setWorkMinutes(workMinutes);
            record.setRemarks(txtRemark.getText().trim());

            recordController.createRecord(record);

            // 8. Update Break Minutes trong Attendance + tính lại tổng/OT
            updateAttendanceBreakTime(attendance.getId());

            // 9. Load table
            loadWorkRecordTable(attendance.getId());

            JOptionPane.showMessageDialog(this, "勤怠記録を正常に追加しました！");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "エラー " + ex.getMessage());
        }
    }//GEN-LAST:event_btnAddRecordActionPerformed


    private void btnUpdateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateRecordActionPerformed
        try {
            int selectedRow = tblRecord.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "更新する勤怠記録を選択してください！");
                return;
            }

            // Lấy ID từ bảng
            int recordId = (int) tblRecord.getValueAt(selectedRow, 0);

            List<WorkRecord> records = recordController.findById(recordId);
            if (records == null || records.isEmpty()) {
                JOptionPane.showMessageDialog(this, "選択された勤怠記録が見つかりません！");
                return;
            }

            WorkRecord existingRecord = records.get(0);

            // Kiểm tra attendance tồn tại
            if (existingRecord.getAttendance() == null) {
                JOptionPane.showMessageDialog(this, "この勤怠記録に関連する出勤データが見つかりません！");
                return;
            }

            // Kiểm tra nếu đã check-out thì chỉ Manager/Admin được sửa
            if (existingRecord.getAttendance().getCheckOutTime() != null && !isManager(loggedInUser)) {
                JOptionPane.showMessageDialog(this, "チェックアウト済みのため、勤怠記録を更新できません！");
                return;
            }

            // Lấy và validate giờ bắt đầu + kết thúc (CHO PHÉP 0〜30h)
            String startStr = fmStart.getText().trim();
            String endStr   = fmEnd.getText().trim();
            if (startStr.isEmpty() || endStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "開始時間と終了時間を入力してください！");
                return;
            }

            LocalTime newStart;
            LocalTime newEnd;
            try {
                newStart = parseFlexibleTime(startStr);
                newEnd   = parseFlexibleTime(endStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "時間の形式が無効です。");
                return;
            }

            long totalMinutes;
            try {
                totalMinutes = calculateMinutesAllowOver24(startStr, endStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "時間の形式が無効です。");
                return;
            }

            if (totalMinutes <= 0) {
                JOptionPane.showMessageDialog(this, "終了時間は開始時間より後でなければなりません！");
                return;
            }

            // Kiểm tra trùng giờ với các record khác
            List<WorkRecord> allRecords = recordController.findByAttendanceId(
                    existingRecord.getAttendance().getId());
            if (allRecords != null) {

                int newStartMin = newStart.getHour() * 60 + newStart.getMinute();
                int newEndMin   = newEnd.getHour() * 60 + newEnd.getMinute();

                for (WorkRecord wr : allRecords) {
                    // Bỏ qua record đang sửa
                    if (wr.getId() == recordId) continue;

                    LocalTime eStart = wr.getStartTime();
                    LocalTime eEnd = wr.getEndTime();
                    if (eStart == null || eEnd == null) continue;

                    int eStartMin = eStart.getHour() * 60 + eStart.getMinute();
                    int eEndMin   = eEnd.getHour() * 60 + eEnd.getMinute();

                    // Kiểm tra overlap
                    if (newStartMin < eEndMin && eStartMin < newEndMin) {
                        JOptionPane.showMessageDialog(this,
                                "この時間帯は既存の勤怠記録と重複しています。\n" +
                                        "既存: " + eStart + "〜" + eEnd);
                        return;
                    }
                }
            }

            // Lấy project + task
            Project project = projectController.findByName((String) cbProject.getSelectedItem()).orElse(null);
            Tasks task     = taskController.findByName((String) cbTask.getSelectedItem()).orElse(null);
            if (project == null || task == null) {
                JOptionPane.showMessageDialog(this, "プロジェクトまたはタスクが見つかりません！");
                return;
            }

            // Break time
            int breakWork = 0;
            if (!txtBreak.getText().trim().isEmpty()) {
                try {
                    breakWork = Integer.parseInt(txtBreak.getText().trim());
                    if (breakWork < 0) {
                        JOptionPane.showMessageDialog(this, "休憩時間は0以上でなければなりません！");
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "休憩時間は数値で入力してください！");
                    return;
                }
            }

            int workMinutes = (int) totalMinutes - breakWork;
            if (workMinutes < 0) {
                JOptionPane.showMessageDialog(this, "休憩時間は総勤務時間を超えることはできません！");
                return;
            }

            // Cập nhật WorkRecord
            existingRecord.setProject(project);
            existingRecord.setTask(task);
            existingRecord.setStartTime(newStart);
            existingRecord.setEndTime(newEnd);
            existingRecord.setBreakWork(breakWork);
            existingRecord.setWorkMinutes(workMinutes);
            existingRecord.setRemarks(txtRemark.getText().trim());

            // Gọi controller update
            WorkRecord success = recordController.updateRecord(existingRecord);

            if (success != null) {
                // Update Break Minutes trong Attendance
                updateAttendanceBreakTime(existingRecord.getAttendance().getId());

                // Load lại bảng
                loadWorkRecordTable(existingRecord.getAttendance().getId());

                // Clear form và reset buttons
                clearRecordForm();
                btnUpdateRecord.setEnabled(false);
                btnAddRecord.setEnabled(true);
                btnDelete.setEnabled(false);

                JOptionPane.showMessageDialog(this, "勤怠記録を正常に更新しました！");
            } else {
                JOptionPane.showMessageDialog(this, "勤怠記録の更新に失敗しました！");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "エラー: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnUpdateRecordActionPerformed


    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        // Xoá WorkRecord xoá mềm (soft delete)
        try {
            int selectedRow = tblRecord.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "削除する勤怠記録を選択してください！");
                return;
            }
            // Lấy ID từ bảng
            int recordId = (int) tblRecord.getValueAt(selectedRow, 0);
            List<WorkRecord> records = recordController.findById(recordId);
            if (records == null || records.isEmpty()) {
                JOptionPane.showMessageDialog(this, "選択された勤怠記録が見つかりません！");
                return;
            }
            WorkRecord existingRecord = records.get(0);
            // Kiểm tra attendance tồn tại
            if (existingRecord.getAttendance() == null) {
                JOptionPane.showMessageDialog(this, "この勤怠記録に関連する出勤データが見つかりません！");
                return;
            }
            // Kiểm tra nếu đã check-out thì chỉ Manager/Admin được xoá
            if (existingRecord.getAttendance().getCheckOutTime() != null && !isManager(loggedInUser)) {
                JOptionPane.showMessageDialog(this, "チェックアウト済みのため、勤怠記録を削除できません！");
                return;
            }
            // Xác nhận xoá
            int confirm = JOptionPane.showConfirmDialog(this,
                    "本当にこの勤怠記録を削除しますか？",
                    "確認",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            // Gọi controller xoá mềm
            boolean deleted = recordController.deleteRecord(recordId);
            if (deleted) {
                // Update Break Minutes trong Attendance
                updateAttendanceBreakTime(existingRecord.getAttendance().getId());
                // Load lại bảng
                loadWorkRecordTable(existingRecord.getAttendance().getId());
                // Clear form và reset buttons
                clearRecordForm();
                btnUpdateRecord.setEnabled(false);
                btnAddRecord.setEnabled(true);
                btnDelete.setEnabled(false);
                JOptionPane.showMessageDialog(this, "勤怠記録を正常に削除しました！");
            } else {
                JOptionPane.showMessageDialog(this, "勤怠記録の削除に失敗しました！");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "エラー: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed



    private void updateAttendanceBreakTime(int attendanceId) {
        try {
            if (attendanceId <= 0) {
                System.err.println("無効な出勤ID： " + attendanceId);
                return;
            }

            // 1. Tính tổng break time của tất cả WorkRecord thuộc attendance này
            int totalBreak = recordController.sumBreakWorkByAttendanceId(attendanceId);

            // 2. Lấy Attendance từ DB (KHÔNG phải List)
            Attendance attendance = attendanceController.findById(attendanceId);

            if (attendance == null) {
                System.err.println("指定されたIDの出勤データが見つかりません： " + attendanceId);
                return;
            }

            // 3. Cập nhật breakMinutes
            attendance.setBreakMinutes(totalBreak);

            // 4. Gọi hàm tính toán lại thời gian nếu attendance có hàm này
            attendance.calculateTimes(); // optional

            // 5. Cập nhật thời gian sửa
            attendance.setUpdatedAt(LocalDateTime.now());

            // 6. Lưu vào database
            attendanceController.update(attendance);

            System.out.println(
                    "Updated attendance break time for ID " + attendanceId +
                            ": " + totalBreak + " minutes"
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating attendance break time for ID " +
                    attendanceId + ": " + e.getMessage());
        }
    }
    private void clearRecordForm() {
        fmStart.setText("");
        fmEnd.setText("");
        txtBreak.setText("");
        txtRemark.setText("");
        // Giữ nguyên Project và Task để tiện nhập tiếp
    }

    //  Được MainDashboard gọi để biết còn ngày nào đang dở không
    public boolean hasUnfinishedAttendance() {
        try {
            // Giả sử bạn có biến currentAttendance lưu attendance đang xem
            if (currentAttendance == null) {
                return false;
            }

            // Chỉ quan tâm ngày đang có check-in nhưng chưa check-out
            return currentAttendance.getCheckInTime() != null
                    && currentAttendance.getCheckOutTime() == null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAttend;
    private javax.swing.JButton btnAddRecord;
    private javax.swing.JButton btnCreatecontent;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdateRecord;
    private javax.swing.JButton btnWorkinformation;
    private javax.swing.JComboBox<String> cbProject;
    //private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JComboBox<String> cbTask;
    private com.toedter.calendar.JDateChooser csDate;
    private javax.swing.JFormattedTextField fmCheckIn;
    private javax.swing.JFormattedTextField fmCheckOut;
    private javax.swing.JFormattedTextField fmEnd;
    private javax.swing.JFormattedTextField fmStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
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
    private javax.swing.JButton btnBack;


    // End of variables declaration//GEN-END:variables
}
