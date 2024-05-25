package com.api.swalayan.user;

import com.api.swalayan.customer.Customer;
import com.api.swalayan.customer.CustomerRepository;
import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface  {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final String CACHE_NAME = "users";
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#a + '_' + #b", condition = "#a <= 100 and #b > 0")
    @Override
    public ResponseEntity<DataResponse> getUserInfo(int a, int b) {
        Pageable pageable= PageRequest.of(a,b, Sort.Direction.DESC,"id");
        Page<User> users=userRepository.findAll(pageable);
        List<UserResponse> list=(b<=100) ? loadFromCache(users) : loadFromDatabase(users);
        return new ResponseEntity<>(new DataResponse("success",true,list, users.getTotalElements(),users.getTotalPages(),a,b), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<DataResponse> filterUser(String firstname,int a,int b) {
        Pageable pageable= PageRequest.of(a,b, Sort.Direction.DESC,"id");
        Page<User> users=userRepository.searchByFirstnameIgnoreCase(firstname,pageable);
        List<UserResponse> list=(b<=100) ? loadFromCache(users) : loadFromDatabase(users);
        return new ResponseEntity<>(new DataResponse("success",true,list, users.getTotalElements(),users.getTotalPages(),a,b), HttpStatus.OK);
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> deleteUser(int id) {
        return userRepository.findById(id).map(
                user->{
                    userRepository.deleteById(id);
                    customerRepository.deleteByUser(user);
                    return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.OK);
                }).orElse(new ResponseEntity<>(new ResponseApi("User Not Found",false), HttpStatus.NOT_FOUND));
    }

    private List<UserResponse> loadFromDatabase(Page<User> users) {
        return users.stream().map(
                user -> {
                    UserResponse userResponse = new UserResponse();
                    userResponse.setId(user.getId());
                    userResponse.setUsername(user.getUsername());
                    userResponse.setFirstname(user.getFirstName());
                    userResponse.setLastname(user.getLastName());
                    userResponse.setEmail(user.getEmail());
                    userResponse.setPhone(user.getPhone());
                    userResponse.setCreatedAt(user.getCreatedAt());
                    userResponse.setUpdatedAt(user.getUpdatedAt());
                    userResponse.setRoles(user.getRole());
                    return userResponse;
                }).toList();
    }

    private List<UserResponse> loadFromCache(Page<User> users) {
        return loadFromDatabase(users);
    }
}
