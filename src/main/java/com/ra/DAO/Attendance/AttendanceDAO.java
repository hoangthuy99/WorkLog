package com.ra.DAO.Attendance;


import com.ra.DTO.request.AttendanceRequest;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class AttendanceDAO implements IAttendanceDAO {

    @Override
    public AttendanceRequest findByUsername(String username) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo username
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Attendance a WHERE a.user.userName = :username";
            return session.createQuery(hql, AttendanceRequest.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(AttendanceRequest attendenceRequest) {
        // TODO:Tạo mới dữ liệu điểm danh
        Transaction transaction = null;
        //TODO: try-with-resources để tự động đóng session
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Thêm attendenceRequest vào DB
            session.save(attendenceRequest);
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
    public void update(AttendanceRequest attendenceRequest) {
    //ToDO:Cập nhật dữ liệu điểm danh
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(attendenceRequest);
            transaction.commit();
            System.out.println("Attendence updated successfully!");
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(AttendanceRequest attendenceRequest) {
        //TODO:Xóa dữ liệu điểm danh
        Transaction transaction = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(attendenceRequest);
            transaction.commit();
            System.out.println("Attendence deleted successfully!");
            return true;
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
            }
    }

    @Override
    public List<AttendanceRequest> findAll() {
        //TODO:Lấy tất cả dữ liệu điểm danh
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<AttendanceRequest> attendenceRequests = session.createQuery("FROM Attendance", AttendanceRequest.class).list();
            return attendenceRequests;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AttendanceRequest> search(String keyword, int page, int size) {
       //TODO:Tìm kiếm dữ liệu điểm danh
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //TODO: Sử dụng JOIN với bảng User để lấy dữ liệu tìm kiếm theo nhân viên
            String hql = "FROM Attendance a JOIN a.user u WHERE u.userName LIKE :keyword";
            List<AttendanceRequest> results = session.createQuery(hql, AttendanceRequest.class)
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
    public Optional<AttendanceRequest> findFindById(int id) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo ID
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            AttendanceRequest attendenceRequest = session.get(AttendanceRequest.class, id);
            return Optional.ofNullable(attendenceRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
