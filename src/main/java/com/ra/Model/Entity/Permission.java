package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;// ma module
    private String name;// module cha
    private int parent_id;// module con thuộc module cha nào
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private List<Roles> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}