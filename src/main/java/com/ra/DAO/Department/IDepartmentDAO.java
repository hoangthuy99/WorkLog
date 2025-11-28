package com.ra.DAO.Department;

import com.ra.Model.Entity.Department;

import java.util.List;
import java.util.Optional;

public interface IDepartmentDAO {
    void create(Department department);
    void update(Department department);
    boolean deleteFindById(int id);
    List<Department> search(String keyword);
    Optional<Department> findFindById(int id);
    List<Department> findAll();
    Optional<Department> findFindByName(String name);
}

