package com.ra.DAO.Atendence;

import com.ra.DTO.request.AttendenceRequest;

import java.util.List;

public interface IAttendenceDAO {
    AttendenceRequest findByUsername(String username);
    void create(AttendenceRequest attendenceRequest);
    void update(AttendenceRequest attendenceRequest);
    boolean delete(AttendenceRequest attendenceRequest);
    List<AttendenceRequest> search(String keyword,int page, int size);

}
