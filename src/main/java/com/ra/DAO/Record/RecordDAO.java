package com.ra.DAO.Record;

import com.ra.Model.Entity.WorkRecord;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        //TODO:Xóa bản ghi công việc theo ID
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(findById(id));
            transaction.commit();
            return true;
        }catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return false;
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
}
