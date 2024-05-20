package com.api.swalayan.auth;

import com.api.swalayan.jwt.JwtService;
import com.api.swalayan.response.LoginResponse;
import com.api.swalayan.response.ResponseApi;
import com.api.swalayan.user.User;
import com.api.swalayan.user.UserMapper;
import com.api.swalayan.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthInterface {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @Override
    public ResponseEntity<ResponseApi> signUp(SignUpRequest sign) {
        User user= UserMapper.toUser(sign);
        userRepository.save(user);
        return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<LoginResponse> login(SignInRequest sign) {

        if(userRepository.findByUsername(sign.getUsername()).isEmpty()){
            return new ResponseEntity<>(new LoginResponse("Login failed",false,null,null),HttpStatus.OK);
        }
        var user=userRepository.findByUsername(sign.getUsername()).orElseThrow();
        var jwt=jwtService.generateToken(user);
        var refreshToken=jwtService.generateRefreshToken(new HashMap<>(),user);
        user.setToken(jwt);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return new ResponseEntity<>(new LoginResponse("success",true,jwt,refreshToken),HttpStatus.OK);
    }
}
