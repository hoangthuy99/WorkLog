package com.ra.Service.Attendance;

import com.ra.Common.Constant;
import com.ra.DAO.Attendance.AttendanceDAO;
import com.ra.DAO.Record.RecordDAO;
import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.Users;
import com.ra.Model.Entity.WorkRecord;

import java.time.LocalDate;
import java.util.List;

public class AttendanceIMPL implements AttendanceService {
    private AttendanceDAO attendanceDAO;
    private RecordDAO recordDAO;
    @Override
    public Attendance getAttendanceWithTotals(int attendanceId) {
        Attendance attendance = (Attendance) attendanceDAO.findFindById(attendanceId);

        if (attendance == null) return null;

        // Lấy work record theo attendanceId
        List<WorkRecord> records = recordDAO.findByAttendanceId(attendanceId);

        int totalMinutes = 0;
        int breakMinutes = 0;
        int overtimeMinutes = 0;

        for (WorkRecord r : records) {
            totalMinutes += r.getWorkMinutes();
            breakMinutes+= r.getBreakWork();

        }

        // Gán lại vào Attendance để View sử dụng
        attendance.setTotalMinutes(totalMinutes);
        attendance.setBreakMinutes(breakMinutes);
        attendance.setOvertimeMinutes(overtimeMinutes);
        attendance.setWorkRecords(records); // để View có thể hiển thị

        return attendance;
    }

    @Override
    public Attendance currentAttendance(Users user) {
        if (user == null) {
            System.out.println("User is null → cannot load attendance.");
            return null;
        }
        LocalDate today = LocalDate.now();

        // 1. Kiểm tra xem hôm nay user đã có attendance chưa
        List<Attendance> attendanceToday = attendanceDAO.findByUserAndDate(user.getId(), today);

        if(attendanceToday == null) {
            System.out.println("Error retrieving attendance records.");
            return null;
        }

        // 2. Nếu chưa có → tạo attendance mới (checkIn + checkOut ban đầu null)
        Attendance newAttendance = new Attendance();
        newAttendance.setUser(user);
        newAttendance.setWorkDate(today);
        newAttendance.setCheckInTime(null);
        newAttendance.setCheckOutTime(null);
        newAttendance.setHoliday(false);
        newAttendance.setStatus(Constant.ATTENDANCE_STATUS_PENDING);

        attendanceDAO.create(newAttendance);

        System.out.println("New attendance created for today.");

        return newAttendance;
    }

    @Override
    public Attendance findByUsername(String username) {
        return null;
    }

    @Override
    public void create(Attendance attendance) {

    }

    @Override
    public void update(Attendance attendance) {

    }

    @Override
    public boolean delete(int id) {
        return false;
    }


    @Override
    public List<Attendance> findAll() {
        return List.of();
    }

    @Override
    public List<Attendance> search(String keyword, int page, int size) {
        return List.of();
    }

    @Override
    public List<Attendance> findFindById(int id) {
        return List.of();
    }

    @Override
    public List<WorkRecord> findByAttendanceId(int attendanceId) {
        return List.of();
    }

    @Override
    public List<Attendance> findByUserAndDate(int userId, LocalDate today) {
        return List.of();
    }



}
