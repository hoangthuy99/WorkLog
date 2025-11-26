package com.ra.DTO.response;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Tasks;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {

    private int id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Roles role;
    private Department department;
    private LocalDateTime deletedAt;
    private List<Tasks> tasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
