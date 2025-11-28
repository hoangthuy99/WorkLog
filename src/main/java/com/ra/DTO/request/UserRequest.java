package com.ra.DTO.request;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Tasks;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserRequest {
    private int id;
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    private String userCode;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters long")
    private String password;

    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Email format is invalid"
    )
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    private List<String> role = new ArrayList<>();
    private Department department;
    private List<Tasks> tasks;
    public static String generateUserCode() {
        return "US" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }

}
