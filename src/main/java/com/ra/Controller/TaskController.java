package com.ra.Controller;

import com.ra.DAO.Task.ITaskDAO;
import com.ra.DAO.Task.TaskDAO;
import com.ra.Model.Entity.Tasks;

import java.util.List;

public class TaskController {

    private final ITaskDAO taskDAO = new TaskDAO();

    /**
     * Tạo mới task
     */
    public void create(Tasks task) {
        try {
            taskDAO.create(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật task
     */
    public void update(Tasks task) {
        try {
            taskDAO.update(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa task theo ID
     */
    public boolean delete(int id) {
        try {
            return taskDAO.deleteFindById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm task theo ID
     */
    public Tasks findById(int id) {
        return taskDAO.findFindById(id).orElse(null);
    }

    /**
     * Tìm task theo tên (dùng cho Edit)
     */
    public Tasks findByName(String name) {
        return taskDAO.findByName(name).orElse(null);
    }

    /**
     * Tìm tất cả task
     */
    public List<Tasks> findAll() {
        return taskDAO.findAll();
    }

    /**
     * Search task theo keyword
     */
    public List<Tasks> search(String keyword) {
        return taskDAO.search(keyword);
    }
}
