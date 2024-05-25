package com.api.swalayan.order.Details;

import com.api.swalayan.produk.Produk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsRequest {
    private Integer produk;
    private Integer order;
    private Integer quantity;
    private Double price;
}
