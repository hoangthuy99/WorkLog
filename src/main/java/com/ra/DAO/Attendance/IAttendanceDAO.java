package com.ra.DAO.Attendance;

import com.ra.DTO.request.AttendanceRequest;
import com.ra.Model.Entity.Attendance;

import java.util.List;
import java.util.Optional;

public interface IAttendanceDAO {
    void create(Attendance attendance);

    void update(Attendance attendance);

    boolean delete(Attendance attendance);

    Optional<Attendance> findById(int id);

    List<Attendance> findAll();

    List<Attendance> search(String keyword, int page, int size);

    Attendance findByUsername(String username);


}