package com.api.swalayan.user;

import com.api.swalayan.auth.SignUpRequest;
import com.api.swalayan.roles.Roles;

public class UserMapper {
    public static User toUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPhone(signUpRequest.getPhone());
        user.setAddress(signUpRequest.getAdress());
        user.setToken("not set");
        user.setRefreshToken("not set");
        user.setRole(signUpRequest.getRole());
        return user;
    }
}
