package com.ra.Service.Department;

import com.ra.DAO.Department.DepartmentDAO;
import com.ra.Model.Entity.Department;

import java.util.List;
import java.util.Optional;

public class DepartmentIMPL implements DepartmentService {

    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    public void create(Department department) {
        departmentDAO.create(department);
    }

    @Override
    public void update(Department department) {
        departmentDAO.update(department);
    }

    @Override
    public boolean deleteFindById(int id) {
        return departmentDAO.deleteFindById(id);
    }

    @Override
    public List<Department> search(String keyword) {
        return departmentDAO.search(keyword);
    }

    @Override
    public Optional<Department> findFindById(int id) {
        return departmentDAO.findFindById(id);
    }

    @Override
    public List<Department> findAll() {
        return departmentDAO.findAll();
    }
}
