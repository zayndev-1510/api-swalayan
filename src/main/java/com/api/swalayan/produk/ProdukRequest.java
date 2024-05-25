package com.api.swalayan.produk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProdukRequest {
    @NotBlank(message = "name product is empty")
    private String name;
    @NotBlank(message = "description product is empty")
    private String description;
    private Double price;
    private Integer stock;
    private String image;
    private String qrcode;
}
