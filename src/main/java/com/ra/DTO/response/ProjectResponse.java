package com.ra.DTO.response;

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
@Setter
@Getter
public class ProjectResponse {
    private int id;
    private String name;
    private List<Department> departments;
    private List<Tasks> tasks;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
