package com.ra.DAO.Attendance;

import com.ra.Model.Entity.Attendance;
import com.ra.Model.Entity.Users;
import com.ra.Model.Entity.WorkRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IAttendanceDAO {
    List<Attendance> findByUsername(String username);
    void create(Attendance attendance);
    void update(Attendance attendance);
    Attendance updateStatus(Attendance attendance);
    boolean delete(int id);
    List<Attendance> findAll();
    List<Attendance> search(String keyword, int page, int size);
    Attendance findById(int id);
    List<WorkRecord> findByAttendanceId(int attendanceId);
    List<Attendance> findByUserAndDate(int userId, LocalDate today);
    List<Attendance> findByAttendanceMonth(int userId, int month, int year);
    List<Attendance> findByUserAndStatus(int userId, int status);
    List<Attendance> findByUsersAndMonth(List<Users> users, int month, int year);
    List<Attendance> findByUsernameAndMonth(String username, int month, int year);
    List<Attendance> findByDepartmentAndMonth(String departmentName, int month, int year);



}