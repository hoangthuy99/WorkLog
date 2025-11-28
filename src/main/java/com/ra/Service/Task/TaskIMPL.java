package com.ra.Service.Task;

import com.ra.DAO.Task.TaskDAO;
import com.ra.Model.Entity.Tasks;

import java.util.List;
import java.util.Optional;

public class TaskIMPL implements TaskSerVice {

    private final TaskDAO taskDAO = new TaskDAO();

    @Override
    public void create(Tasks task) {
        taskDAO.create(task);
    }

    @Override
    public void update(Tasks task) {
        taskDAO.update(task);
    }

    @Override
    public boolean deleteFindById(int id) {
        return taskDAO.deleteFindById(id);
    }

    @Override
    public List<Tasks> search(String keyword) {
        return taskDAO.search(keyword);
    }

    @Override
    public Optional<Tasks> findFindById(int id) {
        return taskDAO.findFindById(id);
    }
    @Override
    public List<Tasks> findAll() {
        return taskDAO.findAll();
    }

    @Override
    public Optional<Tasks> findByName(String name) {
        return taskDAO.findByName(name);
    }


}
