package com.api.swalayan.customer;

import com.api.swalayan.response.DataResponse;
import org.springframework.http.ResponseEntity;

public interface CustomerInterface {
  ResponseEntity<DataResponse> findAllCustomers(int page, int size);
  ResponseEntity<DataResponse> findCustomerById(Long id);
}
