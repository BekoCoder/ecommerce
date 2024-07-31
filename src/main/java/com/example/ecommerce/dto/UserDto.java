package com.example.ecommerce.dto;

import com.example.ecommerce.entity.enums.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private String username;
    private String password;
    private Integer isDeleted = 0;
    private List<UserRole> roles;
}
