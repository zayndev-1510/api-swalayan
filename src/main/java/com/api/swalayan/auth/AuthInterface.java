package com.api.swalayan.auth;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.LoginResponse;
import com.api.swalayan.response.RefreshTokenResponse;
import com.api.swalayan.response.ResponseApi;
import org.springframework.http.ResponseEntity;

public interface AuthInterface {
    ResponseEntity<ResponseApi> signUp(SignUpRequest sign);
    ResponseEntity<LoginResponse> login(SignInRequest sign);
    ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);

}
