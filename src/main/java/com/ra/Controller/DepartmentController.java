package com.ra.Controller;

import com.ra.Model.Entity.Department;
import com.ra.Service.Department.DepartmentIMPL;
import com.ra.Service.Department.DepartmentService;

import java.util.List;

public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController() {
        this.departmentService = new DepartmentIMPL();
    }

    public List<Department> findAll() {
        return departmentService.findAll();
    }

    public void create(Department d) {
        departmentService.create(d);
    }

    public void update(Department d) {
        departmentService.update(d);
    }

    public boolean delete(int id) {
        return departmentService.deleteFindById(id);
    }
    public List<Department> search(String keyword) {
        return departmentService.search(keyword);
    }

}
