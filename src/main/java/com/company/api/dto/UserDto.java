package com.company.api.dto;

import com.company.persistance.entity.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotNull
    private UserRole userRole;
}
