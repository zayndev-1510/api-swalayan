package com.api.swalayan.auth;

import com.api.swalayan.customer.Customer;
import com.api.swalayan.customer.CustomerRepository;
import com.api.swalayan.jwt.JwtService;
import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.LoginResponse;
import com.api.swalayan.response.RefreshTokenResponse;
import com.api.swalayan.response.ResponseApi;
import com.api.swalayan.roles.Roles;
import com.api.swalayan.user.User;
import com.api.swalayan.user.UserMapper;
import com.api.swalayan.user.UserRepository;
import com.api.swalayan.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthInterface {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final String CACHE_NAME="users";

    public  String generateCustomerNumber() {
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyMM"));

        // Cari customer terakhir yang dibuat pada bulan ini
        Customer latestCustomer = customerRepository.findFirstByCustomerNumberStartingWithOrderByCustomerNumberDesc(yearMonth);

        int nextSequence = 1;
        if (latestCustomer != null) {
            String lastSequenceStr = latestCustomer.getCustomerNumber().substring(8);
            nextSequence = Integer.parseInt(lastSequenceStr) + 1;
        }

        return String.format("CUSTO%s%05d", yearMonth, nextSequence);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseApi> signUp(SignUpRequest sign) {

        User user= UserMapper.toUser(sign);
        user.setPassword(passwordEncoder.encode(sign.getPassword()));
        userRepository.save(user);
        if (sign.getRole() != Roles.ADMIN || user.getRole() != Roles.KASIR) {
            Customer customer = new Customer();
            customer.setUser(user); // Associate customer with the user

            // Generate customer number (assuming you have a method for this)
            String customerNumber =generateCustomerNumber();
            customer.setCustomerNumber(customerNumber);
            customerRepository.save(customer);
        }
        return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<LoginResponse> login(SignInRequest sign) {

        if(userRepository.findByUsername(sign.getUsername()).isEmpty()){
            return new ResponseEntity<>(new LoginResponse("Login failed",false,null,null,null),HttpStatus.OK);
        }
        var user=userRepository.findByUsername(sign.getUsername()).orElseThrow();

        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), sign.getPassword()));
            var jwt=jwtService.generateToken(user);
            var refreshToken=jwtService.generateRefreshToken(new HashMap<>(),user);
            user.setToken(jwt);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return new ResponseEntity<>(new LoginResponse("Login berhasil",true,jwt,refreshToken,user.getRole()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new LoginResponse(e.getMessage(),false,null,null,null),HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest refreshToken) {
        String username=jwtService.extractUsername(refreshToken.getToken());
        RefreshTokenResponse refreshTokenResponse=new RefreshTokenResponse();
        User user=userRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("invalid email"));
        if(jwtService.validateToken(refreshToken.getToken(),user)){
            var jwt=jwtService.generateToken(user);
           refreshTokenResponse.setToken(jwt);
           refreshTokenResponse.setRefreshToken(refreshToken.getToken());
        }
        return new ResponseEntity<>(refreshTokenResponse,HttpStatus.OK);
    }
}
