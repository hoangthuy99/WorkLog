package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name = "departmentCode", length = 10, unique = true, nullable = false)
    private String departmentCode;

    // Department - Users (1 - n)
    @OneToMany(mappedBy = "department")
    private List<Users> users;

    // Department - Project (n - n)
    // ⭐ DEPARTMENT LÀ OWNER
    @ManyToMany
    @JoinTable(
            name = "department_project",
            joinColumns = @JoinColumn(name = "departmentId"),
            inverseJoinColumns = @JoinColumn(name = "projectId")
    )
    private List<Project> projects;

    // Department - Task (n - n)
    @ManyToMany
    @JoinTable(
            name = "department_task",
            joinColumns = @JoinColumn(name = "departmentId"),
            inverseJoinColumns = @JoinColumn(name = "taskId")
    )
    private List<Tasks> tasks;

    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return name;
    }

    public static String generateDepartmentCode() {
        return "DP" + UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 8).toUpperCase();
    }
}
