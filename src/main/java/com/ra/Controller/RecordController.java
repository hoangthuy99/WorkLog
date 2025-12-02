package com.ra.Controller;

import com.ra.DAO.Record.RecordDAO;
import com.ra.Model.Entity.WorkRecord;

import java.util.List;

public class RecordController {
    private RecordDAO recordDAO;
    public RecordController(RecordDAO recordDAO) {
        this.recordDAO = recordDAO;
    }
    public WorkRecord createRecord(WorkRecord workRecord) {
        return recordDAO.create(workRecord);
    }
    public WorkRecord updateRecord(WorkRecord workRecord) {
        return recordDAO.update(workRecord);
    }
    public WorkRecord deleteRecord(WorkRecord workRecord) {
        recordDAO.deleteFindById(workRecord.getId());
        return workRecord;
    }
    public List<WorkRecord> findById(int id) {
        return recordDAO.findById(id);
    }
    public List<WorkRecord> findAll() {
        return recordDAO.findAll();
    }
    public List<WorkRecord> searchRecords(String keyword, int page, int size) {
        return recordDAO.search(keyword, page, size);
    }

}
