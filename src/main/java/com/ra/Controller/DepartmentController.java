package com.ra.Controller;

import com.ra.DAO.Department.DepartmentDAO;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;


import java.util.List;
import java.util.Optional;

public class DepartmentController {

    private final DepartmentDAO departmentService = new DepartmentDAO();

    public List<Department> findAll() {
        return departmentService.findAll();
    }

    public Department create(String name, List<Project> projects, List<Tasks> tasks) {
        Department d = new Department();
        d.setName(name);
        d.setDepartmentCode(Department.generateDepartmentCode());
        d.setProjects(projects);
        d.setTasks(tasks);

        departmentService.create(d);
        return d;
    }

    public boolean update(Department d) {
        departmentService.update(d);
        return true;
    }

    public boolean delete(int id) {
        return departmentService.deleteFindById(id);
    }

    public List<Department> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll(); // trả lại full khi không nhập
        }

        String lower = keyword.toLowerCase();

        return findAll()
                .stream()
                .filter(d ->
                        d.getName().toLowerCase().contains(lower) ||

                                // search theo project
                                (d.getProjects() != null && d.getProjects().stream()
                                        .anyMatch(p -> p.getName().toLowerCase().contains(lower))) ||

                                // search theo task
                                (d.getTasks() != null && d.getTasks().stream()
                                        .anyMatch(t -> t.getName().toLowerCase().contains(lower)))
                )
                .toList();
    }


    public Department findById(int id) {
        return departmentService.findFindByIdFetchAll(id).orElse(null);
    }

    /** Load projects + tasks */
    public void loadRelations(Department d) {
        if (d == null) return;
        d.getProjects().size(); // force load
        d.getTasks().size();
    }

    public Department create(Department d) {
        departmentService.create(d);
        return d;
    }

    public Optional<Department> findFindById(int id) {
        return departmentService.findFindByIdFetchAll(id);
    }


}
