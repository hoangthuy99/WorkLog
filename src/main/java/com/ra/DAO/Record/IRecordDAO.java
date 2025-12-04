package com.ra.DAO.Record;

import com.ra.Model.Entity.WorkRecord;

import java.util.List;
import java.util.Optional;

public interface IRecordDAO {
    WorkRecord create(WorkRecord workRecord);
    WorkRecord update(WorkRecord workRecord);
    boolean deleteFindById(int id);
    List<WorkRecord> findAll();
    List<WorkRecord> search(String keyword, int page, int size);
    List<WorkRecord> findById(int id);
    List<WorkRecord> findByAttendanceId(int attendanceId);

}