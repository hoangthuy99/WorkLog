package com.ra.Controller;

import com.ra.DAO.Record.IRecordDAO;
import com.ra.DAO.Record.RecordDAO;
import com.ra.Model.Entity.WorkRecord;

public class RecordController {

    private final IRecordDAO recordDAO;

    public RecordController() {
        this.recordDAO = new RecordDAO();
    }

    public boolean create(WorkRecord wr) {
        try {
            recordDAO.create(wr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(WorkRecord wr) {
        try {
            recordDAO.update(wr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            return recordDAO.deleteFindById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<WorkRecord> findAll() {
        return recordDAO.findAll();
    }

    public java.util.List<WorkRecord> search(String keyword, int page, int size) {
        return recordDAO.search(keyword, page, size);
    }

    public java.util.Optional<WorkRecord> findById(int id) {
        return recordDAO.findById(id);
    }
}
