package com.api.swalayan.customer;

import com.api.swalayan.user.User;
import com.api.swalayan.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customer_number;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse user;
}
