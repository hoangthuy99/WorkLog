package com.ra.DAO.Task;

import com.ra.Model.Entity.Tasks;

import java.util.List;
import java.util.Optional;


public interface ITaskDAO {
    void create(Tasks task);
    void update(Tasks task);
    boolean deleteFindById(int id);
    List<Tasks> search(String keyword);
    Optional<Tasks> findFindById(int id);

    List<Tasks> findAll();
    Optional<Tasks> findByName(String name);
}