package com.ra.DAO.Project;

import com.ra.Model.Entity.Project;

import java.util.List;
import java.util.Optional;

public interface IProjectDAO
{
    void create(Project project);
    void update(Project project);
    boolean deleteFindById(int id);
    List<Project> findAll();
    List<Project> search(String keyword, int page, int size);
    Project findFindById(int id);
    Optional<Project> findByName(String name);
}