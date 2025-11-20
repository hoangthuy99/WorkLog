package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "projectCode", length = 10, unique = true, nullable = false)
    private String projectCode;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "departmentProject",
            joinColumns = @JoinColumn(name = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "departmentId")
    )
    private List<Department> departments;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_project",
            joinColumns = @JoinColumn(name = "taskId"),
            inverseJoinColumns = @JoinColumn(name = "projectId")
    )
    private List<Tasks> tasks;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static String generateProjectCode() {
        return "PD" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }
}
