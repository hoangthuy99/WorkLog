package com.ra;

import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Khởi động ứng dụng với Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            System.out.println("Hibernate kết nối thành công!");
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
//         Đảm bảo GUI chạy trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Tạo instance của dashboard form
//            DashboardForm dashboard = new DashboardForm();
//            dashboard.setVisible(true);
        });
    }
}
