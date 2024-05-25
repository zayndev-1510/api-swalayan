package com.api.swalayan.order.Details;

import com.api.swalayan.order.OrderResponse;
import com.api.swalayan.produk.ProdukResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsResponse {
    private Long orderitem;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderResponse order;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProdukResponse produk;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer quantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double total;
}
