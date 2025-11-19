package com.ra.DAO.Atendence;


import com.ra.DTO.request.AttendenceRequest;

import java.util.List;

public class AttendenceDAO implements IAttendenceDAO{

    @Override
    public AttendenceRequest findByUsername(String username) {
        return null;
    }

    @Override
    public void create(AttendenceRequest attendenceRequest) {

    }

    @Override
    public void update(AttendenceRequest attendenceRequest) {

    }

    @Override
    public boolean delete(AttendenceRequest attendenceRequest) {
        return false;
    }

    @Override
    public List<AttendenceRequest> search(String keyword, int page, int size) {
        return List.of();
    }
}
