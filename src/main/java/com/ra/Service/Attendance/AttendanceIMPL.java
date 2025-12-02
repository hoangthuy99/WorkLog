package com.ra.Service.Attendance;

import com.ra.DAO.Attendance.AttendanceDAO;
import com.ra.DAO.Record.RecordDAO;
import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.WorkRecord;

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
            breakMinutes += r.getBreakMinutes();

        }

        // Gán lại vào Attendance để View sử dụng
        attendance.setTotalMinutes(totalMinutes);
        attendance.setBreakMinutes(breakMinutes);
        attendance.setOvertimeMinutes(overtimeMinutes);
        attendance.setWorkRecords(records); // để View có thể hiển thị

        return attendance;
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
}
