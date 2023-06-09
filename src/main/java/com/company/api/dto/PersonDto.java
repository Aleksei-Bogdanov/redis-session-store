package com.company.api.dto;

import com.company.persistance.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private long id;
    private String username;
    private UserRole userRole;
}
