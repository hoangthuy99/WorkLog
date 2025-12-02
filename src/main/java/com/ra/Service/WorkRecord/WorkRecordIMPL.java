package com.ra.Service.WorkRecord;

import com.ra.DAO.Attendance.AttendanceDAO;
import com.ra.DAO.Record.RecordDAO;
import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.WorkRecord;

import java.util.List;
import java.util.Optional;

public class WorkRecordIMPL implements WorkRecordService {
    private AttendanceDAO attendanceDAO;
    private RecordDAO recordDAO;
    @Override
    public Attendance getAttendanceWithTotals(int attendanceId) {
        return null;
    }

    @Override
    public WorkRecord create(WorkRecord workRecord) {
        return null;
    }

    @Override
    public WorkRecord update(WorkRecord workRecord) {
        return null;
    }

    @Override
    public boolean deleteFindById(int id) {
        return false;
    }

    @Override
    public List<WorkRecord> findAll() {
        return List.of();
    }

    @Override
    public List<WorkRecord> search(String keyword, int page, int size) {
        return List.of();
    }

    @Override
    public List<WorkRecord> findById(int id) {
        return List.of();
    }

    @Override
    public List<WorkRecord> findByAttendanceId(int attendanceId) {

       List<Attendance> attendance = attendanceDAO.findFindById(attendanceId);

        if (attendance == null) return null;

        List<WorkRecord> records = recordDAO.findByAttendanceId(attendanceId);

        // Tính tổng
        int totalMinutes = 0;
        int breakMinutes = 0;
        int overtimeMinutes = 0;

        for (WorkRecord r : records) {
            totalMinutes += r.getWorkMinutes();
            breakMinutes += r.getBreakMinutes();
        }

        // Gán lại vào Attendance
        attendance.get(0).setBreakMinutes(breakMinutes);
        attendance.get(0).setOvertimeMinutes(overtimeMinutes);
        attendance.get(0).setTotalMinutes(totalMinutes);
       ;

        return records;
    }
}
