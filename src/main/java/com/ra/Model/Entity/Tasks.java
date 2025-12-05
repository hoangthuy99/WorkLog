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
@Table(name = "tasks")
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(name = "taskCode", length = 10, unique = true, nullable = false)
    private String taskCode;


    // Task - Project (n - n)
    @ManyToMany
    @JoinTable(
            name = "task_project",
            joinColumns = @JoinColumn(name = "taskId"),
            inverseJoinColumns = @JoinColumn(name = "projectId")
    )
    private List<Project> projects;

    // Task - Department (n - n)
    @ManyToMany
    @JoinTable(
            name = "department_task",
            joinColumns = @JoinColumn(name = "taskId"),
            inverseJoinColumns = @JoinColumn(name = "departmentId")
    )
    private List<Department> departments;


    // Task - User (n - n)
    @ManyToMany(mappedBy = "tasks")
    private List<Users> users;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static String generateTaskCode() {
        return "TS" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }
    @Override
    public String toString() {
        return this.name;    // hoặc getName()
    }

}

