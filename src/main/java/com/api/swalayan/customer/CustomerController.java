package com.api.swalayan.customer;

import com.api.swalayan.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customers/")
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping(value = "/{a}/{b}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DataResponse> getCustomer(@PathVariable("a") int a, @PathVariable("b") int b) {
        return customerService.findAllCustomers(a,b);
    }
    @GetMapping("/{id}")
    ResponseEntity<DataResponse> getCustomerById(@PathVariable("id") long id) {
        return customerService.findCustomerById(id);
    }
}
