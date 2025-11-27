package com.ra.Controller;

import com.ra.Model.Entity.Project;
import com.ra.Service.Project.ProjectIMPL;
import com.ra.Service.Project.ProjectService;


import java.util.List;

public class ProjectController {

    private final ProjectService projectService;

    public ProjectController() {
        this.projectService = new ProjectIMPL();
    }

    public void create(Project project) {
        projectService.create(project);
    }

    public void update(Project project) {
        projectService.update(project);
    }

    public boolean delete(int id) {
        return projectService.deleteFindById(id);
    }

    public List<Project> findAll() {
        return projectService.findAll();
    }

    public List<Project> search(String keyword, int page, int size) {
        return projectService.search(keyword, page, size);
    }

    public Project findById(int id) {
        return projectService.findFindById(id);
    }
}
