package com.ra.Service.WorkRecord;

import com.ra.Model.Entity.Attendance;

public interface WorkRecordService {
    Attendance getAttendanceWithTotals(int attendanceId);
}