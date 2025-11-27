package com.ra.DAO.Attendance;

import com.ra.DTO.request.AttendanceRequest;

import java.util.List;
import java.util.Optional;

public interface IAttendanceDAO {
    AttendanceRequest findByUsername(String username);
    void create(AttendanceRequest attendenceRequest);
    void update(AttendanceRequest attendenceRequest);
    boolean delete(AttendanceRequest attendenceRequest);
    List<AttendanceRequest> findAll();
    List<AttendanceRequest> search(String keyword, int page, int size);
    Optional<AttendanceRequest> findFindById(int id);


}