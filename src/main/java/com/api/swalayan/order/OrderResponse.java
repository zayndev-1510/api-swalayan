package com.api.swalayan.order;

import com.api.swalayan.customer.CustomerResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResponse {
    private Integer order;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CustomerResponse customer;
}
