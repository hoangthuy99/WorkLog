package com.ra.Service.WorkRecord;

import com.ra.DAO.Record.IRecordDAO;
import com.ra.Model.Entity.Attendance;

public interface WorkRecordService extends IRecordDAO {
    Attendance getAttendanceWithTotals(int attendanceId);
}
