package com.ra.Controller;

import com.ra.Common.Constant;
import com.ra.DAO.Attendance.AttendanceDAO;
import com.ra.Model.Entity.Attendance;

import java.util.List;

public class AttendanceController {

    private final AttendanceDAO attendanceDAO;

    public AttendanceController() {
        this.attendanceDAO = new AttendanceDAO();
    }

    // Create
    public boolean create(Attendance attendance) {
        try {
            attendanceDAO.create(attendance);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update
    public boolean update(Attendance req) {
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
        return attendanceDAO.delete(id);
    }

    // Find all
    public List<Attendance> findAll() {
        return attendanceDAO.findAll();
    }

    // Search by keyword (userName)
    public List<Attendance> search(String keyword, int page, int size) {
        return attendanceDAO.search(keyword, page, size);
    }

    // Find by ID
    public List<Attendance> findById(int id) {
        return attendanceDAO.findFindById(id);
    }

    // Find by username
    public Attendance findByUsername(String username) {
        return attendanceDAO.findByUsername(username);
    }
}
