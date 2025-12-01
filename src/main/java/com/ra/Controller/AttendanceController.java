package com.ra.Controller;

import com.ra.DAO.Attendance.IAttendanceDAO;
import com.ra.DAO.Attendance.AttendanceDAO;
import com.ra.DTO.request.AttendanceRequest;

import java.util.List;
import java.util.Optional;

public class AttendanceController {

    private final IAttendanceDAO attendanceDAO;

    public AttendanceController() {
        this.attendanceDAO = new AttendanceDAO();
    }

    // Create
    public boolean create(AttendanceRequest req) {
        try {
            attendanceDAO.create(req);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update
    public boolean update(AttendanceRequest req) {
        try {
            attendanceDAO.update(req);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete
    public boolean delete(int id) {
        try {
            Optional<AttendanceRequest> a = attendanceDAO.findFindById(id);
            if (a.isEmpty()) return false;
            return attendanceDAO.delete(a.get());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find all
    public List<AttendanceRequest> findAll() {
        return attendanceDAO.findAll();
    }

    // Search by keyword (userName)
    public List<AttendanceRequest> search(String keyword, int page, int size) {
        return attendanceDAO.search(keyword, page, size);
    }

    // Find by ID
    public Optional<AttendanceRequest> findById(int id) {
        return attendanceDAO.findFindById(id);
    }

    // Find by username
    public AttendanceRequest findByUsername(String username) {
        return attendanceDAO.findByUsername(username);
    }
}
