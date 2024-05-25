package com.api.swalayan.user;

import com.api.swalayan.auth.AuthService;
import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{a}/{b}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> getUser(@PathVariable("a") int a, @PathVariable("b") int b) {
        return userService.getUserInfo(a,b);
    }
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseApi> deleteUser(@PathVariable("id") int id) {
        return userService.deleteUser(id);
    }

    @GetMapping(value = "/{a}/{b}/{c}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> getUser(@PathVariable("a") String firstname, @PathVariable("b") int number,@PathVariable("c") int size) {
        return userService.filterUser(firstname,number,size);
    }

}
