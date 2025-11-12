package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
