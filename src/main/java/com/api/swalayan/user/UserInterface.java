package com.api.swalayan.user;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import org.springframework.http.ResponseEntity;

public interface UserInterface {
    ResponseEntity<DataResponse> getUserInfo(int a,int b);
    ResponseEntity<DataResponse> filterUser(String firstname,int number,int size);
    ResponseEntity<ResponseApi> deleteUser(int a);
}
