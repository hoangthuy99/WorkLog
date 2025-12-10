package com.ra.DTO.request;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Tasks;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectRequest {

    private int id;
    private String name;

    private String projectCode;
    // Project - Department (n - n)

    private List<Department> departments;
    // Project - Task (n - n)
    private List<Tasks> tasks;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
