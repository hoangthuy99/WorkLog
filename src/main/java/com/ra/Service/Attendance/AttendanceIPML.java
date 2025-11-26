package com.ra.Service.Attendance;

import com.ra.DAO.Attendance.AttendanceDAO;
import com.ra.DTO.request.AttendanceRequest;

import java.util.List;

public class AttendanceIPML implements AttendanceService {
    private AttendanceDAO attendenceDAO;
    @Override
    public AttendanceRequest findByUsername(String username) {
        return attendenceDAO.findByUsername(username);
    }

    @Override
    public void create(AttendanceRequest attendenceRequest) {
        attendenceDAO.create(attendenceRequest);
    }

    @Override
    public void update(AttendanceRequest attendenceRequest) {
attendenceDAO.update(attendenceRequest);
    }

    @Override
    public boolean delete(AttendanceRequest attendenceRequest) {
        return attendenceDAO.delete(attendenceRequest);
    }

    @Override
    public List<AttendanceRequest> search(String keyword, int page, int size) {
        return attendenceDAO.search(keyword,page,size);
    }
}
