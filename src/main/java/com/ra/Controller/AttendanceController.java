package com.ra.Controller;

import com.ra.DAO.Attendance.IAttendanceDAO;
import com.ra.DAO.Attendance.AttendanceDAO;
import com.ra.DTO.request.AttendanceRequest;
import com.ra.Model.Entity.Attendance;

import java.util.List;
import java.util.Optional;

public class AttendanceController {

    private final IAttendanceDAO attendanceDAO;

    public AttendanceController() {
        this.attendanceDAO = new AttendanceDAO();
    }

    // =============================
    // CREATE Attendance (DTO → ENTITY)
    // =============================
    public Attendance create(AttendanceRequest req) {

        Attendance a = new Attendance();
        a.setUser(req.getUser());
        a.setWorkDate(req.getWorkDate());
        a.setCheckInTime(req.getCheckInTime());
        a.setCheckOutTime(req.getCheckOutTime());
        a.setBreakTime(req.getBreakTime());
        a.setIsHoliday(req.getIsHoliday());
        a.setStatus(req.getStatus());

        a.setTotalMinutes((int) (req.getTotalHours() * 60));        // convert hours → minutes
        a.setOvertimeMinutes((int) (req.getOvertimeHours() * 60));  // convert hours → minutes

        attendanceDAO.create(a);  // save entity

        return a;                 // return để lấy ID khi Insert WorkRecord
    }

    // =============================
    // UPDATE Attendance
    // =============================
    public boolean update(int id, AttendanceRequest req) {

        Optional<Attendance> opt = attendanceDAO.findById(id);
        if (opt.isEmpty()) return false;

        Attendance a = opt.get();

        a.setWorkDate(req.getWorkDate());
        a.setCheckInTime(req.getCheckInTime());
        a.setCheckOutTime(req.getCheckOutTime());
        a.setBreakTime(req.getBreakTime());
        a.setIsHoliday(req.getIsHoliday());
        a.setStatus(req.getStatus());
        a.setTotalMinutes((int) (req.getTotalHours() * 60));
        a.setOvertimeMinutes((int) (req.getOvertimeHours() * 60));

        attendanceDAO.update(a);

        return true;
    }

    // =============================
    // DELETE Attendance
    // =============================
    public boolean delete(int id) {
        Optional<Attendance> opt = attendanceDAO.findById(id);
        if (opt.isEmpty()) return false;
        return attendanceDAO.delete(opt.get());
    }

    // =============================
    // FIND ALL
    // =============================
    public List<Attendance> findAll() {
        return attendanceDAO.findAll();
    }

    // =============================
    // SEARCH by username
    // =============================
    public List<Attendance> search(String keyword, int page, int size) {
        return attendanceDAO.search(keyword, page, size);
    }

    // =============================
    // FIND by ID
    // =============================
    public Optional<Attendance> findById(int id) {
        return attendanceDAO.findById(id);
    }

    // =============================
    // FIND by username
    // =============================
    public Attendance findByUsername(String username) {
        return attendanceDAO.findByUsername(username);
    }

}
