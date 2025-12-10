package com.ra.DAO.Attendance;


import com.ra.DAO.Record.RecordDAO;
import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.Users;
import com.ra.Model.Entity.WorkRecord;
import com.ra.Utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class AttendanceDAO implements IAttendanceDAO {
    private RecordDAO recordDAO;

    @Override
    public List<Attendance> findByUsername(String username) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo username
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Attendance a WHERE a.user.userName = :username";
            List<Attendance> attendances = session.createQuery(hql, Attendance.class)
                    .setParameter("username", username)
                    .list();
            return attendances;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void create(Attendance attendance) {
        // TODO:Tạo mới dữ liệu điểm danh
        Transaction transaction = null;
        //TODO: try-with-resources để tự động đóng session
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Thêm attendenceRequest vào DB
            session.merge(attendance);
            // Commit transaction
            transaction.commit();
            System.out.println("Attendence created successfully!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // rollback nếu lỗi
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Attendance attendance) {
        //ToDO:Cập nhật dữ liệu điểm danh
        Transaction transaction = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(attendance);
            transaction.commit();
            System.out.println("Attendence updated successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Attendance updateStatus(Attendance attendance) {
        Transaction transaction = null;
        Attendance managed = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // merge trả về entity đã được quản lý bởi Hibernate
            managed = (Attendance) session.merge(attendance);

            transaction.commit();
            System.out.println("Attendance status updated successfully!");

            return managed; // trả về bản đã merge

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null; // cho caller biết là failed
        }
    }


    @Override
    public boolean delete(int id) {
        //TODO:Xóa dữ liệu điểm danh theo ID xoá mềm
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Attendance attendance = session.get(Attendance.class, id);
            if (attendance != null) {
                attendance.setDeletedAt(true);  // Đánh dấu xóa mềm
                session.update(attendance);                // Cập nhật thay vì delete
                transaction.commit();
                System.out.println("Attendance deleted successfully!");
                return true;
            } else {
                System.out.println("Attendance not found with id: " + id);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
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
    public Attendance findById(int id) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo ID
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Attendance a WHERE a.id = :id";
            Attendance attendance = session.createQuery(hql, Attendance.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return attendance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Attendance> findByDate(LocalDate workDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Sử dụng JOIN FETCH để load tất cả quan hệ cần thiết
            String hql = "SELECT DISTINCT a FROM Attendance a " +
                    "LEFT JOIN FETCH a.user u " + // Load user
                    "LEFT JOIN FETCH u.department " + // Load department nếu cần
                    "WHERE a.workDate = :workDate " +
                    "ORDER BY u.fullName";

            Query<Attendance> query = session.createQuery(hql, Attendance.class);
            query.setParameter("workDate", workDate);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
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
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Attendance> findByAttendanceMonth(int userId, int month, int year) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo userId, month và year
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Attendance a " +
                    "LEFT JOIN FETCH a.user u " +
                    "LEFT JOIN FETCH u.department d " +
                    "LEFT JOIN FETCH u.role " +
                    "WHERE a.user.id = :userId " +
                    "AND MONTH(a.workDate) = :month " +
                    "AND YEAR(a.workDate) = :year " +
                    "ORDER BY a.workDate";

            List<Attendance> attendances = session.createQuery(hql, Attendance.class)
                    .setParameter("userId", userId)
                    .setParameter("month", month)
                    .setParameter("year", year)
                    .list();

            return attendances != null ? attendances : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @Override
    public List<Attendance> findByDepartmentAndMonth(String departmentName, int month, int year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Lấy tất cả users trong department
            String userHql = "SELECT u.id FROM Users u " +
                    "LEFT JOIN u.department d " +
                    "WHERE d.name = :deptName AND u.deletedAt IS NULL";

            List<Integer> userIds = session.createQuery(userHql, Integer.class)
                    .setParameter("deptName", departmentName)
                    .list();

            if (userIds.isEmpty()) {
                return new ArrayList<>();
            }

            // Lấy attendance của tất cả users trong department
            String attendanceHql = "FROM Attendance a " +
                    "LEFT JOIN FETCH a.user u " +
                    "LEFT JOIN FETCH u.department d " +
                    "LEFT JOIN FETCH u.role " +
                    "WHERE a.user.id IN :userIds " +
                    "AND MONTH(a.workDate) = :month " +
                    "AND YEAR(a.workDate) = :year " +
                    "ORDER BY u.userName, a.workDate";

            List<Attendance> attendances = session.createQuery(attendanceHql, Attendance.class)
                    .setParameter("userIds", userIds)
                    .setParameter("month", month)
                    .setParameter("year", year)
                    .list();

            return attendances != null ? attendances : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @Override
    public List<Attendance> findByUsernameAndMonth(String username, int month, int year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Tìm userId từ username
            String userHql = "SELECT u.id FROM Users u WHERE u.userName = :username";

            Integer userId = session.createQuery(userHql, Integer.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (userId == null) {
                return new ArrayList<>();
            }

            // Sử dụng hàm có sẵn
            return findByAttendanceMonth(userId, month, year);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @Override
    public List<Attendance> findByUsersAndMonth(List<Users> users, int month, int year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (users == null || users.isEmpty()) {
                return new ArrayList<>();
            }

            // Lấy danh sách userIds
            List<Integer> userIds = users.stream()
                    .map(Users::getId)
                    .collect(Collectors.toList());

            String hql = "FROM Attendance a " +
                    "LEFT JOIN FETCH a.user u " +
                    "LEFT JOIN FETCH u.department d " +
                    "LEFT JOIN FETCH u.role " +
                    "WHERE a.user.id IN :userIds " +
                    "AND MONTH(a.workDate) = :month " +
                    "AND YEAR(a.workDate) = :year " +
                    "ORDER BY u.userName, a.workDate";

            List<Attendance> attendances = session.createQuery(hql, Attendance.class)
                    .setParameter("userIds", userIds)
                    .setParameter("month", month)
                    .setParameter("year", year)
                    .list();

            return attendances != null ? attendances : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Attendance> findByUserAndStatus(int userId, int status) {
        //TODO:Tìm kiếm dữ liệu điểm danh theo userId và status
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Attendance a WHERE a.user.id = :userId AND a.status = :status";
            List<Attendance> attendances = session.createQuery(hql, Attendance.class)
                    .setParameter("userId", userId)
                    .setParameter("status", status)
                    .list();
            return attendances;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
