package com.ra.DAO.Record;

import com.ra.Model.Entity.WorkRecord;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RecordDAO implements IRecordDAO {

    @Override
    public WorkRecord create(WorkRecord workRecord) {
        //TODO: Tạo mới bản ghi công việc vào DB
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Thêm workRecord vào DB
            session.save(workRecord);
            // Commit transaction
            transaction.commit();
            System.out.println("WorkRecord created successfully!");
        }catch (Exception e){
            if (transaction != null){
                transaction.rollback(); // rollback nếu lỗi
            }
            e.printStackTrace();
        }
        return workRecord;
    }

    @Override
    public WorkRecord update(WorkRecord workRecord) {
        //TODO:Cập nhật bản ghi công việc
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(workRecord);
            transaction.commit();
            System.out.println("WorkRecord updated successfully!");
        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return workRecord;
    }

    @Override
    public boolean deleteFindById(int id) {
        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            System.out.println("=== DEBUG deleteFindById START ===");
            System.out.println("Deleting record ID: " + id);

            // Cách 1: Load entity và update
            WorkRecord record = session.get(WorkRecord.class, id);
            if (record == null) {
                System.out.println("ERROR: Record not found in database!");
                if (transaction != null) transaction.rollback();
                return false;
            }

            System.out.println("DEBUG: Found record:");
            System.out.println("  ID: " + record.getId());
            System.out.println("  Current isDeleted: " + record.isDeleted());
            System.out.println("  Current deletedAt: " + record.getDeletedAt());

            // Kiểm tra xem record đã bị xóa chưa
            if (record.isDeleted()) {
                System.out.println("WARNING: Record already deleted!");
                transaction.rollback();
                return false;
            }

            // Gọi phương thức softDelete
            System.out.println("DEBUG: Calling softDelete() method");
            record.softDelete(); // Sử dụng phương thức helper
            record.setUpdatedAt(LocalDateTime.now());

            System.out.println("DEBUG: After softDelete:");
            System.out.println("  isDeleted: " + record.isDeleted());
            System.out.println("  deletedAt: " + record.getDeletedAt());
            System.out.println("  updatedAt: " + record.getUpdatedAt());

            // Update record
            System.out.println("DEBUG: Updating record in session...");
            session.update(record);

            // Flush để xem SQL sẽ được thực thi
            System.out.println("DEBUG: Flushing session...");
            session.flush();

            transaction.commit();
            System.out.println("DEBUG: Transaction committed!");

            // Kiểm tra lại sau commit
            session.clear(); // Clear cache
            WorkRecord afterRecord = session.get(WorkRecord.class, id);
            if (afterRecord != null) {
                System.out.println("DEBUG: After commit check:");
                System.out.println("  isDeleted: " + afterRecord.isDeleted());
                System.out.println("  deletedAt: " + afterRecord.getDeletedAt());
            }

            System.out.println("=== DEBUG deleteFindById END ===");
            return true;

        } catch (Exception e) {
            System.err.println("ERROR in deleteFindById: " + e.getMessage());
            e.printStackTrace();
            if (transaction != null) {
                try {
                    transaction.rollback();
                    System.out.println("DEBUG: Transaction rolled back due to error");
                } catch (Exception rollbackEx) {
                    System.err.println("Error rolling back: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                } catch (Exception closeEx) {
                    System.err.println("Error closing session: " + closeEx.getMessage());
                }
            }
        }
    }

    @Override
    public List<WorkRecord> findAll() {
        // TODO: Lấy tất cả bản ghi công việc
        Transaction transaction = null;
        List<WorkRecord> workRecords = List.of();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Đã đúng: Thêm điều kiện isDeleted = false
            workRecords = session.createQuery(
                    "FROM WorkRecord WHERE isDeleted = false",
                    WorkRecord.class
            ).getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return workRecords;
    }

    @Override
    public List<WorkRecord> search(String keyword, int page, int size) {
        //TODO:Tìm kiếm bản ghi công việc
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM WorkRecord wr WHERE wr.attendance.user.userName  LIKE :keyword";
            return session.createQuery(hql, WorkRecord.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();

        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<WorkRecord> findById(int id) {
        //TODO:Tìm kiếm bản ghi công việc theo ID
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // SỬA: Thêm điều kiện isDeleted = false
            String hql = "FROM WorkRecord wr WHERE wr.id = :id AND wr.isDeleted = false";
            List<WorkRecord> workRecords = session.createQuery(hql, WorkRecord.class)
                    .setParameter("id", id)
                    .getResultList();

            transaction.commit();
            return workRecords;
        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public List<WorkRecord> findByAttendanceId(int attendanceId) {
        //TODO:Tìm kiếm bản ghi công việc theo Attendance ID
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();

            // SỬA: Thêm điều kiện isDeleted = false
            String hql = "FROM WorkRecord wr WHERE wr.attendance.id = :attendanceId " +
                    "AND wr.isDeleted = false " +
                    "ORDER BY wr.startTime";

            List<WorkRecord> workRecords = session.createQuery(hql, WorkRecord.class)
                    .setParameter("attendanceId", attendanceId)
                    .getResultList();

            return workRecords;
        }catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Integer sumBreakWorkByAttendanceId(int id) {
        //TODO:Tính tổng thời gian nghỉ theo Attendance ID
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // SỬA: Thêm điều kiện isDeleted = false
            String hql = "SELECT COALESCE(SUM(wr.breakWork), 0) " +
                    "FROM WorkRecord wr " +
                    "WHERE wr.attendance.id = :id " +
                    "AND wr.isDeleted = false";

            Long result = session.createQuery(hql, Long.class)
                    .setParameter("id", id)
                    .uniqueResult();

            transaction.commit();
            return Math.toIntExact(result != null ? result : 0);

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                } catch (Exception closeEx) {
                    System.err.println("Error closing session: " + closeEx.getMessage());
                }
            }
        }
    }
}
