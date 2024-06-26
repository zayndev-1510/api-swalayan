package com.api.swalayan.response;

import com.api.swalayan.roles.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String message;
    private Boolean success;
    private String token;
    private String refreshToken;
    private Roles roles;
}
