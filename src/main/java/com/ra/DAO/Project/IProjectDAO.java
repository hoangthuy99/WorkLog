package com.ra.DAO.Project;

import com.ra.Model.Entity.Project;

import java.util.List;

public interface IProjectDAO
{
    void create(Project project);
    void update(Project project);
    boolean deleteFindById(int id);
    List<Project> findAll();
    List<Project> search(String keyword, int page, int size);
    Project findFindById(int id);
}
