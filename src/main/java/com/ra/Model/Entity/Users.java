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
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    @Column(name = "userCode", length = 10, unique = true, nullable = false)
    private String userCode;
    private String password;
    private String email;
    private String fullName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId", nullable = false)
    private Roles role;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId")
    private Department department;
    private LocalDateTime deletedAt;
    @ManyToMany
    @JoinTable(
            name = "user_task", // bảng mô tả 1 user có thể quản lý nhiều task
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "taskId")
    )
    private List<Tasks> tasks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Phương thức tạo mã người dùng duy nhất
    public static String generateUserCode() {
        return "US" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }

    public boolean isEmpty() {
        return userName == null || userName.isBlank()
                || password == null || password.isBlank();
    }

    public Users get() {
        return this;
    }

}
