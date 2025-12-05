package com.ra.Controller;

import com.ra.DAO.Department.DepartmentDAO;
import com.ra.DAO.Project.ProjectDAO;
import com.ra.DAO.Task.TaskDAO;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import java.util.List;

public class ProjectController {

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final TaskDAO taskDAO = new TaskDAO();


    public Project create(String name, List<Department> departments, List<Tasks> tasks) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Project name cannot be empty");
            return null;
        }

        Project project = new Project();
        project.setName(name);
        project.setProjectCode(Project.generateProjectCode());
        project.setDepartments(departments);
        project.setTasks(tasks);

        projectDAO.create(project);
        return project;
    }
    public void create(Project project) {
        projectDAO.create(project); // DAO sẽ throw exception
    }



    public List<Project> findAll() {
        List<Project> list = projectDAO.findAll();
        for (Project p : list) projectDAO.loadRelations(p);
        return list;
    }


    public List<Project> search(String keyword) {
        List<Project> list = projectDAO.search(keyword, 1, 999);
        for (Project p : list) projectDAO.loadRelations(p);
        return list;
    }


    public boolean delete(int id) {
        return projectDAO.deleteFindById(id);
    }


    public Project findById(int id) {
        return projectDAO.findFindById(id);
    }


    public void loadRelations(Project p) {
        projectDAO.loadRelations(p);
    }


    public void update(Project p) {
        projectDAO.update(p);
    }


    // For combobox
    public List<Department> getAllDepartments() {
        return departmentDAO.findAll();
    }

    public List<Tasks> getAllTasks() {
        return taskDAO.findAll();
    }
}
