package com.ra.Attendance;

import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.WorkRecord;

import java.time.LocalDate;
import java.util.List;

public interface IAttendanceDAO {
    Attendance findByUsername(String username);
    void create(Attendance attendance);
    void update(Attendance attendance);
    boolean delete(int id);
    List<Attendance> findAll();
    List<Attendance> search(String keyword, int page, int size);
    List<Attendance> findFindById(int id);
    List<WorkRecord> findByAttendanceId(int attendanceId);
    List<Attendance> findByUserAndDate(int userId, LocalDate today);

}