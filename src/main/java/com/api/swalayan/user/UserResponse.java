package com.api.swalayan.user;

import com.api.swalayan.roles.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String username;
    private Roles roles;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
