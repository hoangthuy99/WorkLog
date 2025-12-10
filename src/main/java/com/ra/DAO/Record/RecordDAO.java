package com.ra.DAO.Record;

import com.ra.Model.Entity.WorkRecord;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.Field;
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
        //TODO:Xóa mềm bản ghi trên wordRecord
        // Soft delete implementation:
        // 1) Try HQL update on common boolean flag fields
        // 2) If none matched, try reflection to set a boolean field on the entity
        // 3) If still not possible, perform a hard delete
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            String[] boolFields = {"deleted", "isDeleted", "active", "enabled"};

            for (String field : boolFields) {
                String hql = "UPDATE WorkRecord wr SET wr." + field + " = :val WHERE wr.id = :id";
                int updated = session.createQuery(hql)
                        .setParameter("val", false)
                        .setParameter("id", id)
                        .executeUpdate();
                if (updated > 0) {
                    transaction.commit();
                    return true;
                }
            }
            // If HQL didn't update anything, try to fetch the entity and set a boolean field via reflection
            WorkRecord workRecord = session.get(WorkRecord.class, id);
            if (workRecord == null) {
                transaction.commit();
                return false;
            }

            boolean reflected = false;
            for (String fieldName : boolFields) {
                try {
                    Field f = workRecord.getClass().getDeclaredField(fieldName);
                    f.setAccessible(true);
                    Class<?> t = f.getType();
                    if (t == boolean.class || t == Boolean.class) {
                        f.set(workRecord, Boolean.FALSE);
                        session.update(workRecord);
                        reflected = true;
                        break;
                    }
                } catch (NoSuchFieldException ignored) {
                    // try next candidate
                }
            }
            if (reflected) {
                transaction.commit();
                return true;
            }

            // Fallback: hard delete if no soft-delete field exists
            session.delete(workRecord);
            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            e.printStackTrace();
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
        //TODO:Lấy tất cả bản ghi công việc
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            List<WorkRecord> workRecords = session.createQuery("from WorkRecord", WorkRecord.class).list();
            transaction.commit();
            return workRecords;
        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return List.of();
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
            WorkRecord workRecord = session.get(WorkRecord.class, id);
            transaction.commit();
            return List.of(workRecord);
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
            String hql = "FROM WorkRecord wr WHERE wr.attendance.id = :attendanceId";
            List<WorkRecord> workRecords = session.createQuery(hql, WorkRecord.class)
                    .setParameter("attendanceId", attendanceId)
                    .list();
            return workRecords;
        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
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

            String hql = "SELECT COALESCE(SUM(wr.breakWork), 0) FROM WorkRecord wr WHERE wr.attendance.id = :id";
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
