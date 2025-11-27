package com.ra.DTO.request;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskRequest {
    private int id;
    private String name;
    private String taskCode;
    // Task - Project (n - n)
    private List<Project> projects;
    // Task - User (n - n)
    private List<Users> users;
    // Task - Department (n - n)
    private List<Department> departments;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
