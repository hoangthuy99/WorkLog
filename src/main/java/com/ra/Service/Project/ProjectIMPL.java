package com.ra.Service.Project;

import com.ra.Model.Entity.Project;

import java.util.List;

public class ProjectIMPL implements ProjectService {
    @Override
    public void create(Project project) {

    }

    @Override
    public void update(Project project) {

    }

    @Override
    public boolean deleteFindById(int id) {
        return false;
    }

    @Override
    public List<Project> findAll() {
        return List.of();
    }

    @Override
    public List<Project> search(String keyword, int page, int size) {
        return List.of();
    }

    @Override
    public Project findFindById(int id) {
        return null;
    }
}
