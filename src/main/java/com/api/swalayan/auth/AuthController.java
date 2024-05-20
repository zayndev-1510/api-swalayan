package com.api.swalayan.auth;

import com.api.swalayan.response.LoginResponse;
import com.api.swalayan.response.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {


    private final AuthService authService;
    @GetMapping
    String hello(){
        return "Hello World";
    }
    @PostMapping(value = "user",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseApi> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @PostMapping(value = "sigin",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LoginResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.login(signInRequest);
    }
}
