package com.ra.Service.Attendance;

import com.ra.DTO.request.AttendanceRequest;

import java.util.List;

public interface AttendanceService
{
    AttendanceRequest findByUsername(String username);
    void create(AttendanceRequest attendenceRequest);
    void update(AttendanceRequest attendenceRequest);
    boolean delete(AttendanceRequest attendenceRequest);
    List<AttendanceRequest> search(String keyword, int page, int size);
}

