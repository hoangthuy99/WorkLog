package com.ra.Service.Project;

import com.ra.DAO.Project.ProjectDAO;
import com.ra.Model.Entity.Project;

import java.util.List;
import java.util.Optional;

public class ProjectIMPL implements ProjectService {

    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public void create(Project project) {
        projectDAO.create(project);
    }

    @Override
    public void update(Project project) {
        projectDAO.update(project);
    }

    @Override
    public boolean deleteFindById(int id) {
        return projectDAO.deleteFindById(id);
    }

    @Override
    public List<Project> findAll() {
        return projectDAO.findAll();
    }

    @Override
    public List<Project> search(String keyword, int page, int size) {
        return projectDAO.search(keyword, page, size);
    }

    @Override
    public Project findFindById(int id) {
        return projectDAO.findFindById(id);
    }

    @Override
    public Optional<Project> findByName(String name) {
        return Optional.empty();
    }


}
