package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;

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
    @OneToMany(mappedBy = "department")
    private List<Users> users;
    @ManyToMany(mappedBy = "departments")
    private List<Project> projects;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Override
    public String toString() {
        return name;
    }
    // Phương thức tạo mã phòng ban tự động duy nhất
    public static String generateDepartmentCode() {
        return "DP" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }
}
