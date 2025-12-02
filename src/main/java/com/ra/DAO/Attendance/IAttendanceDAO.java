package com.ra.DAO.Attendance;

import com.ra.Model.Entity.Attendance;

import java.util.List;
import java.util.Optional;

public interface IAttendanceDAO {
    Attendance findByUsername(String username);
    void create(Attendance attendance);
    void update(Attendance attendance);
    boolean delete(int id);
    List<Attendance> findAll();
    List<Attendance> search(String keyword, int page, int size);
    List<Attendance> findFindById(int id);


}