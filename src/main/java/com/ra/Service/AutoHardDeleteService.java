package com.ra.Service;

import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class AutoHardDeleteService {
    public static void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction tx = session.beginTransaction();

                    session.createQuery("""
                        DELETE FROM WorkRecord 
                        WHERE isDeleted = true 
                        AND deletedAt < :limit
                    """)
                            .setParameter("limit", LocalDateTime.now().minusMonths(1))
                            .executeUpdate();

                    tx.commit();

                    System.out.println("Hard delete job executed!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Nghỉ 24 giờ rồi chạy lại
                try {
                    Thread.sleep(24 * 60 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setDaemon(true); // Tự tắt khi app tắt
        thread.start();
    }
}
