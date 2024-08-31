package com.example.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "valyuta bo'yicha ma'lumotlar")
public class CurrencyDto {

    @Schema(description = "valyuta nomi")
    private String title;

    @Schema(description = "valyuta birligi")
    private String code;

    @Schema(description = "Valyutaning markaziy bankdagi narxi")
    private Double cb_price;

    @Schema(description = "Nbu sotib oladigan narx")
    private Double nbu_buy_price;

    @Schema(description = "Nbu sotib oladigan narx")
    private Double nbu_cell_price;

    @Schema(description = "Sana")
    private String date;
}
