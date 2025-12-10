package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private int id;
    private String userName;
    private String userCode;
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
