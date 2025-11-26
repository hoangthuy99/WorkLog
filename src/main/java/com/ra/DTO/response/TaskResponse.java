package com.ra.DTO.response;

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
public class TaskResponse {
    private int id;
    private String name;
    private String description;
    private Project project;
    private List<Users> users;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
