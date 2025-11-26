package com.ra;

import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            System.out.println("Hibernate kết nối thành công!");
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
