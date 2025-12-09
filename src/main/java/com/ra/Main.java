package com.ra;


import com.ra.Utils.HibernateUtil;
import com.ra.View.holidays.AddHoliday;
import com.ra.View.holidays.AllHoliday;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //Kết nối với Hibernate
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            System.out.println("Kết nối thành công");
        }
        // Tạo khung
        /*JFrame frame = new JFrame("All Holiday Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Gọi panel AddHoliday
        AllHoliday panel = new AllHoliday();

        // Thêm panel vào khung
        frame.add(panel);

        // Hiển thị
        frame.setVisible(true);*/

    }
}
