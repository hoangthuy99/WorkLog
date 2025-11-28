package com.ra.Service.Task;

import com.ra.DAO.Task.ITaskDAO;
import com.ra.Model.Entity.Tasks;

import java.util.List;

public interface TaskSerVice extends ITaskDAO {
    List<Tasks> findAll();
}