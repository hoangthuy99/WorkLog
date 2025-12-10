package com.ra.DTO.request;

import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;
import com.ra.Model.Entity.Users;
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
public class DepartmentRequest {
    private int id;
    private String name;

    private String departmentCode;
    // Department - Users (1 - n)

    private List<Users> users;
    // Department - Project (n - n)

    private List<Project> projects;
    // Department - Task (n - n)

    private List<Tasks> tasks;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public String toString() {
        return name;
    }
}
