package com.api.swalayan.auth;

import com.api.swalayan.roles.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "username is empty")
    @Size(min = 4,max = 20,message = "Username minimal 4 and maksimal 20")
    private String username;
    @NotBlank(message = "password is empty")
    private String password;
    @NotBlank(message = "email is empty")
    @Email(message = "format email")
    private String email;

    @NotBlank(message = "phone is empty")
    private String phone;
    private Roles role;

    @NotBlank(message = "firstname is empty")
    private String firstName;
    @NotBlank(message = "lastname is empty")
    private String lastName;
    @NotBlank(message = "address is empty")
    private String adress;
}
