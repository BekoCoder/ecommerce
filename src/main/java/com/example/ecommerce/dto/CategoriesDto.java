package com.example.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Kategoriya bo'yicha ma'lumotlar")
public class CategoriesDto {
    @Schema(description = "Kategoriya nomi")
    private String name;
}
