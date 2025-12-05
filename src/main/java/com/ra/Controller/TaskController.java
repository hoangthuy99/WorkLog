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
        taskDAO.create(task);
    }


    /**
     * Cập nhật task
     */
    public void update(Tasks task) {
        taskDAO.update(task);
    }


    /**
     * Xóa task theo ID
     */
    public void delete(int id) {
        try {
            boolean success = taskDAO.deleteFindById(id);

            if (!success) {
                throw new RuntimeException("タスク削除に失敗しました。");
            }

        } catch (RuntimeException e) {
            // Ném lại lỗi nghiệp vụ từ DAO
            throw e;
        } catch (Exception e) {
            // Lỗi khác
            throw new RuntimeException("削除中にエラーが発生しました。", e);
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
