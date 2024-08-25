package com.example.ecommerce.dto;

import com.example.ecommerce.entity.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "User ma'lumotlari")
public class UserDto {

    @Schema(description = "User Ismi")
    private String name;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Parol")
    private String password;

    @Schema(description = "User Roli")
    private List<UserRole> roles;
}
