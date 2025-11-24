package com.ra.DTO.request;

import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Users;
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
public class DepartmentRequest {
    private int id;
    private String name;
    private String departmentCode;
    private List<Users> users;
    private List<Project> projects;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
