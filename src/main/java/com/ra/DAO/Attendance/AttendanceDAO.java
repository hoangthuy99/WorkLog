package com.ra.DAO.Attendance;


import com.ra.DAO.Record.RecordDAO;
import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.WorkRecord;
import com.ra.Service.WorkRecord.WorkRecordIMPL;
import com.ra.Utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AttendanceDAO implements IAttendanceDAO {
   private RecordDAO recordDAO;
   private WorkRecordIMPL workRecordIMPL;

    @Override
    public Attendance findByUsername(String username) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo username
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Attendance a WHERE a.user.userName = :username";
            return session.createQuery(hql, Attendance.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(Attendance attendance) {
        // TODO:Tạo mới dữ liệu điểm danh
        Transaction transaction = null;
        //TODO: try-with-resources để tự động đóng session
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Thêm attendenceRequest vào DB
            session.merge(attendance);
            // Commit transaction
            transaction.commit();
            System.out.println("Attendence created successfully!");
        }catch (Exception e){
            if (transaction != null){
                transaction.rollback(); // rollback nếu lỗi
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Attendance attendance) {
    //ToDO:Cập nhật dữ liệu điểm danh
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(attendance);
            transaction.commit();
            System.out.println("Attendence updated successfully!");
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(int id) {
        //TODO:Xóa dữ liệu điểm danh theo ID
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Attendance a WHERE a.id = :id";
            Attendance attendance = session.createQuery(hql, Attendance.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (attendance != null) {
                transaction = session.beginTransaction();
                session.delete(attendance);
                transaction.commit();
                System.out.println("Attendance deleted successfully!");
                return true;
            } else {
                System.out.println("Attendance not found with id: " + id);
                return false;
            }
        }
    }


    @Override
    public List<Attendance> findAll() {
        //TODO:Lấy tất cả dữ liệu điểm danh
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Attendance> attendances = session.createQuery("FROM Attendance", Attendance.class).list();
            return attendances;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Attendance> search(String keyword, int page, int size) {
       //TODO:Tìm kiếm dữ liệu điểm danh
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //TODO: Sử dụng JOIN với bảng User để lấy dữ liệu tìm kiếm theo nhân viên
            String hql = "FROM Attendance a JOIN a.user u WHERE u.userName LIKE :keyword";
            List<Attendance> results = session.createQuery(hql, Attendance.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Attendance> findFindById(int id) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo ID
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Attendance a WHERE a.id = :id";
            List<Attendance> attendance = session.createQuery(hql, Attendance.class)
                    .setParameter("id", id)
                    .list();
            return attendance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<WorkRecord> findByAttendanceId(int attendanceId) {
       //TODO:Lấy danh sách bản ghi công việc theo attendanceId
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM WorkRecord wr WHERE wr.attendance.id = :attendanceId";
            List<WorkRecord> workRecords = session.createQuery(hql, WorkRecord.class)
                    .setParameter("attendanceId", attendanceId)
                    .list();
            return workRecords;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Attendance> findByUserAndDate(int userId, LocalDate today) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo userId và date
       try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Attendance a WHERE a.user.id = :userId AND a.workDate = :workDate";
            List<Attendance> attendances = session.createQuery(hql, Attendance.class)
                    .setParameter("userId", userId)
                    .setParameter("workDate", today)
                    .list();
            return attendances;
        }catch (Exception e) {
            e.printStackTrace();
            return List.of();
       }
    }


}
