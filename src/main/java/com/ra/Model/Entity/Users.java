package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
    private LocalDateTime deletedAt;
    @ManyToMany
    @JoinTable(
            name = "user_task", // bảng mô tả 1 user có thể quản lý nhiều task
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "content_id")
    )
    private List<Tasks> tasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
