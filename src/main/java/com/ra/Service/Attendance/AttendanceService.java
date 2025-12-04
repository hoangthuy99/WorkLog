package com.ra.Service.Attendance;

import com.ra.DAO.Attendance.IAttendanceDAO;
import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.Users;

public interface AttendanceService extends IAttendanceDAO {
    Attendance getAttendanceWithTotals(int attendanceId);
    Attendance currentAttendance(Users user);
}
