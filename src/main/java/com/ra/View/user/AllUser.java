package com.ra.View.user;

import com.ra.Controller.UserController;
import com.ra.Model.Entity.Tasks;
import com.ra.Model.Entity.Users;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.lang.reflect.Constructor;


/**
 *
 * @author Admin
 */
public class AllUser extends JPanel {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AllUser.class.getName());

    // KHAI BÁO BIẾN CHO LOGIC XỬ LÝ
    private int currentPage = 1;
    private final int pageSize = Integer.MAX_VALUE;
    // Biến này lưu trữ ID thật của các user đang hiển thị trên bảng
    private List<Integer> userIds;
    public UserController userController;
    private Users loggedInUser;

    private boolean isManager(Users user) {
        if (user == null || user.getRole() == null) return false;
        int roleId = user.getRole().getId();   // 1=EMP, 2=MANAGER, 3=ADMIN
        return roleId == 2 || roleId == 3;
    }


    /**
     * Creates new form AllUser1
     */
    public AllUser(Users user) {
        this.loggedInUser = user;
        userController = new UserController();
        initComponents();
        applyCenteredLayout();
        loadUserTable();
        setColumnWidths();

        // 🔹 Ẩn nút tạo user nếu là MANAGER (nhưng vẫn cho ADMIN thấy)
        if (loggedInUser != null && loggedInUser.getRole() != null) {
            int roleId = loggedInUser.getRole().getId();
            if (roleId == 2) { // chỉ manager
                btnAdduser.setVisible(false);
                btnEdit.setVisible(false);
                btnDelete.setVisible(false);
            }
            // nếu muốn ẩn luôn cho cả admin thì dùng: if (!isManager(loggedInUser)) ...
        }
    }








    // Phương thức mới để thiết lập chiều rộng cột cố định
    private void setColumnWidths() {
        if (tbAllUser.getColumnModel().getColumnCount() < 7) return;

        TableColumnModel columnModel = tbAllUser.getColumnModel();

        // 🌟 ĐIỀU CHỈNH: Giảm tổng chiều rộng cột từ 700 xuống 690 để tránh thanh cuộn ngang
        // Tổng: 40 + 90 + 95 + 95 + 100 + 90 + 180 = 690

        // Cột 0: STT
        columnModel.getColumn(0).setPreferredWidth(40);
        // Cột 1: ユーザーコード (User Code)
        columnModel.getColumn(1).setPreferredWidth(120);
        // Cột 2: 社員名 (Full Name)
        columnModel.getColumn(2).setPreferredWidth(110);
        // Cột 3: ユーザー名 (User Name)
        columnModel.getColumn(3).setPreferredWidth(110);
        // Cột 4: 部署 (Department)
        columnModel.getColumn(4).setPreferredWidth(110);
        // Cột 5: ロール (Role)
        columnModel.getColumn(5).setPreferredWidth(90);
        // Cột 6: タスク (Tasks) - Chiếm phần còn lại (tăng lên 180)
        columnModel.getColumn(6).setPreferredWidth(110);
    }

    private void applyCenteredLayout() {

        btnSearch.addActionListener(this::btnSearchActionPerformed);
        btnAlluser.addActionListener(this::btnAlluserActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        txtSearch.addActionListener(this::txtSearchActionPerformed);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        int TABLE_WIDTH = 700;
        int BUTTON_WIDTH = 75;
        int BUTTON_GAP = 30; // Khoảng cách giữa Edit và Delete
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                // Khoảng trống co giãn bên trái để căn giữa
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false) // Thêm false để giữ kích thước cố định

                                        // Hàng 1: Tìm kiếm và Thêm mới (Chiều rộng khớp với bảng)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                // Nhóm tìm kiếm bên trái
                                                .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAlluser)

                                                // Khoảng trống co giãn
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                                // Nút thêm mới bên phải
                                                .addComponent(btnAdduser, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))

                                        // Hàng 2: Bảng (rộng cố định)
                                        .addComponent(scrollTable, GroupLayout.PREFERRED_SIZE, TABLE_WIDTH, GroupLayout.PREFERRED_SIZE)

                                        // Hàng 3: Nút Edit/Delete (căn phải so với bảng)
                                        .addGroup(layout.createSequentialGroup()
                                                // Khoảng trống co giãn để đẩy nút sang phải
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Dùng Short.MAX_VALUE để đẩy
                                                .addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE)
                                                .addGap(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP)
                                                .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE)))
                                // Khoảng trống co giãn bên phải để căn giữa
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // ====================================================================
        // BỐ CỤC DỌC (VERTICAL GROUP)
        // ====================================================================
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                // THAY THẾ padding cố định bằng KHOẢNG TRỐNG CO GIÃN
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                // Hàng 1: Tìm kiếm/Tạo mới
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAlluser)
                                        .addComponent(btnAdduser))
                                .addGap(18, 18, 18)
                                // Hàng 2: Bảng
                                .addComponent(scrollTable, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE) // Chiều cao cố định
                                .addGap(18, 18, 18)
                                // Hàng 3: Hành động
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))

                                // Giữ khoảng trống co giãn ở dưới
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    public void loadUserTable() {
        String keyword = txtSearch.getText().trim();  // lấy từ khóa người dùng nhập

        List<Users> list;

        if (keyword.isEmpty()) {
            list = userController.findAll();          // không có keyword → load tất cả
        } else {
            list = userController.findAll(keyword, currentPage, pageSize);  // có keyword → search
        }

        userIds = new java.util.ArrayList<>();

        String[][] data = new String[list.size()][7];

        for (int i = 0; i < list.size(); i++) {
            Users u = list.get(i);
            data[i][0] = String.valueOf((currentPage - 1) * pageSize + i + 1);
            data[i][1] = u.getUserCode();
            data[i][2] = u.getFullName();
            data[i][3] = u.getUserName();
            data[i][4] = u.getDepartment() != null ? u.getDepartment().getName() : "";
            data[i][5] = u.getRole() != null ? u.getRole().getName() : "";
            data[i][6] = (u.getTasks() != null && !u.getTasks().isEmpty())
                    ? u.getTasks().stream().map(Tasks::getName).collect(Collectors.joining(", "))
                    : "";
            userIds.add(u.getId());
        }


        tbAllUser.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"No.", "ユーザーコード", "社員名", "ユーザー名", "部署", "ロール","タスク"}
        ));
        setColumnWidths();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearch = new JTextField();
        btnSearch = new JButton();
        btnAlluser = new JButton();
        btnAdduser = new JButton();
        scrollTable = new JScrollPane();
        tbAllUser = new JTable();
        btnEdit = new JButton();
        btnDelete = new JButton();

        setBackground(new Color(255, 255, 255));

        txtSearch.setToolTipText("");
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAlluser.setText("すべて");
        btnAlluser.addActionListener(this::btnAlluserActionPerformed);

        btnAdduser.setBackground(new Color(201, 221, 201));
        btnAdduser.setText("新ユーザー作成");
        // LƯU Ý: Dòng này đã được giữ lại để đảm bảo sự kiện được gán
        btnAdduser.addActionListener(this::btnAdduserActionPerformed);

        tbAllUser.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null}
                },
                new String [] {
                        "ID", "ユーザーコード", "社員名", "ユーザー名", "部署", "ロール", "タスク"
                }
        ) {
            Class[] types = new Class [] {
                    Integer.class, String.class, String.class, String.class, String.class, String.class, String.class
            };
            boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        // 🌟 SỬA ĐỔI: Tắt Auto Resize để cho phép điều chỉnh chiều rộng cột
        tbAllUser.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollTable.setViewportView(tbAllUser);
        // 🌟 SỬA ĐỔI: Giảm chiều rộng ưu tiên của scrollTable xuống 700
        scrollTable.setPreferredSize(new Dimension(700, 300));


        btnEdit.setBackground(UIManager.getColor("Button.background"));
        btnEdit.setText("編集");
        // LƯU Ý: Dòng này vẫn còn và là lệnh gán sự kiện ĐẦU TIÊN
        btnEdit.addActionListener(this::btnEditActionPerformed);

        btnDelete.setBackground(UIManager.getColor("Button.background"));
        btnDelete.setText("削除");
        //btnDelete.addActionListener(this::btnDeleteActionPerformed);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                // Giảm padding bên trái (từ 37 xuống 20 để cân bằng)
                                .addGap(20, 20, 20)
                                .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAlluser)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdduser, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
                                // Giảm padding bên phải (từ 42 xuống 20 để cân bằng)
                                .addGap(20, 20, 20))
                        .addGroup(layout.createSequentialGroup()
                                // 🌟 SỬA ĐỔI: Dùng khoảng trống co giãn để căn giữa
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(scrollTable, GroupLayout.PREFERRED_SIZE, 700, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEdit)
                                .addGap(45, 45, 45)
                                .addComponent(btnDelete)
                                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAlluser)
                                        .addComponent(btnAdduser))
                                .addGap(18, 18, 18)
                                // 🌟 SỬA ĐỔI: Tăng chiều cao scrollTable lên 300
                                .addComponent(scrollTable, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEdit)
                                        .addComponent(btnDelete))
                                .addContainerGap(55, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // ========================================================
    // EVENT HANDLERS
    // ========================================================

    private void txtSearchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        btnSearchActionPerformed(evt);
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnSearchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        currentPage = 1;
        loadUserTable();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAlluserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAlluserActionPerformed
        currentPage = 1;
        txtSearch.setText("");
        loadUserTable();
    }//GEN-LAST:event_btnAlluserActionPerformed

    private void btnAdduserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAdduserActionPerformed
        try {
            // 1. Lấy cửa sổ cha
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;

            // 2. Khởi tạo AddUser JPanel và truyền tham chiếu AllUser (this)
            JPanel addUserPanel;
            try {
                // Giả định constructor của AddUser nhận AllUser làm đối số đầu tiên
                Class<?> addUserClass = Class.forName("com.ra.View.user.AddUser");
                Constructor<?> constructor = addUserClass.getConstructor(AllUser.class);
                addUserPanel = (JPanel) constructor.newInstance(this);
            } catch (ClassNotFoundException e) {
                // Xử lý trường hợp không tìm thấy lớp AddUser
                JOptionPane.showMessageDialog(this, "Lớp AddUser không tìm thấy. Đảm bảo AddUser.java đã tồn tại và nằm trong package com.ra.View.user.", "Lỗi Cấu hình", JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, "Lỗi khi khởi tạo AddUser", e);
                return;
            } catch (NoSuchMethodException e) {
                // Xử lý trường hợp không tìm thấy constructor AddUser(AllUser)
                JOptionPane.showMessageDialog(this, "Lớp AddUser cần có constructor AddUser(AllUser parentPanel).", "Lỗi Cấu hình", JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, "Lỗi Constructor AddUser", e);
                return;
            }


            // 3. Tạo JDialog để chứa JPanel AddUser và hiển thị modal
            JDialog dialog = new JDialog(parentFrame, "新ユーザー作成", true);
            dialog.setContentPane(addUserPanel);

            // Cài đặt hành động khi đóng và hiển thị
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.pack();
            dialog.setLocationRelativeTo(parentFrame);

            // Thêm Listener để refresh bảng khi Dialog đóng
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadUserTable();
                }
            });

            // Lệnh hiển thị dialog duy nhất
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xảy ra lỗi khi mở form AddUser: " + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "Lỗi khi mở AddUser Panel", e);
        }

    }//GEN-LAST:event_btnAdduserActionPerformed

    private void btnEditActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int selectedRow = tbAllUser.getSelectedRow();

        if (selectedRow < 0 || selectedRow >= userIds.size()) {
            JOptionPane.showMessageDialog(this, "ユーザーを選択してください！");
            return;
        }

        try {
            Integer id = userIds.get(selectedRow);

            Optional<Users> user = Optional.ofNullable(userController.findById(id));
            if (user.isPresent()) {

                Users userToEdit = user.get();

                // EditUser là JFrame: Khởi tạo trực tiếp và gọi setVisible(true) MỘT LẦN ở đây

                // Khởi tạo EditUser (là JFrame)
                EditUser editForm = new EditUser(userToEdit, this);

                // Lấy cửa sổ cha (để căn giữa form)
                Window parentWindow = SwingUtilities.getWindowAncestor(this);

                // Cài đặt hành động khi đóng
                editForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Hiển thị cửa sổ Edit và căn giữa
                editForm.pack();
                editForm.setLocationRelativeTo(parentWindow);
                editForm.setVisible(true); // 🌟 CHỈ CÓ MỘT LỆNH HIỂN THỊ DUY NHẤT 🌟

            } else {
                JOptionPane.showMessageDialog(this, "ユーザーが存在しません！");
            }
        } catch (NoClassDefFoundError e) {
            // Xử lý trường hợp quên import hoặc EditUser chưa compile
            JOptionPane.showMessageDialog(this, "EditUser クラスが見つかりません。EditUser.java が正しくインポートされ、コンパイルされていることを確認してください。", "エラー", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "EditUser の ClassNotFound エラー", e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ユーザーの取得中にエラーが発生しました。\n" + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "ユーザー編集ID取得エラー", e);
        }

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tbAllUser.getSelectedRow();

        if (row < 0 || row >= userIds.size()) {
            JOptionPane.showMessageDialog(this, "ユーザーを選択してください！");
            return;
        }

        try {
            Integer id = userIds.get(row);

            Optional<Users> u = Optional.ofNullable(userController.findById(id));

            if (!u.isPresent()) {
                JOptionPane.showMessageDialog(this, "このユーザーは存在しません");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "このユーザーを削除しますか？",
                    "確認",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                userController.deleteUser(id);

                loadUserTable();

                JOptionPane.showMessageDialog(this, "削除しました"); // Đã xóa
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ユーザー削除エラー： " + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "ユーザー削除エラー", e);
        }
    }



    private JButton btnAdduser;
    private JButton btnAlluser;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnSearch;
    private JScrollPane scrollTable;
    private JTable tbAllUser;
    private JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}