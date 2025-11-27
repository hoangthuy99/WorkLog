package com.ra.Controller;

import com.ra.Model.Entity.Tasks;
import com.ra.Service.Task.TaskIMPL;
import com.ra.Service.Task.TaskSerVice;

import java.util.List;
import java.util.Optional;

public class TaskController {

    private final TaskSerVice taskService;

    public TaskController() {
        this.taskService = new TaskIMPL();   // khởi tạo Service
    }

    public void create(Tasks task) {
        taskService.create(task);
    }

    public void update(Tasks task) {
        taskService.update(task);
    }

    public boolean delete(int id) {
        return taskService.deleteFindById(id);
    }


    public List<Tasks> search(String keyword) {
        return taskService.search(keyword);
    }

    public Optional<Tasks> findById(int id) {
        return taskService.findFindById(id);
    }

    public List<Tasks> findAll() {
        return taskService.findAll();
    }

}
