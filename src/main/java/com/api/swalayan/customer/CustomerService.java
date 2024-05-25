package com.api.swalayan.customer;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerInterface{

    private final CustomerRepository customerRepository;
    private final String CACHE_NAME = "customers";
    private final String DETAILS_CACHE_NAME = "customer_details";

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#page + '_' + #size", condition = "#size <= 100 and #size > 0")
    @Override
    public ResponseEntity<DataResponse> findAllCustomers(int page, int size) {
        Pageable pageable= PageRequest.of(page,size, Sort.Direction.DESC,"id");
        Page<Customer> customers = customerRepository.findAll(pageable);
        List<CustomerResponse> list=(page<=100) ? loadFromCache(customers) : loadFromDatabase(customers);
        return new ResponseEntity<>(
                new DataResponse(
                        "success",true,
                        list,customers.getTotalElements(),
                        customers.getTotalPages(),
                        page,size), HttpStatus.OK);
    }
    private List<CustomerResponse> loadFromCache(Page<Customer> customers) {
        return loadFromDatabase(customers);
    }

    private List<CustomerResponse> loadFromDatabase(Page<Customer> customers) {
        return customers.stream().map(
                customer -> {
                    CustomerResponse customerResponse = new CustomerResponse();
                    customerResponse.setId(customer.getId());
                    customerResponse.setCustomer_number(customer.getCustomerNumber());
                    UserResponse user=getUserDetail(customer);
                    customerResponse.setUser(user);
                    return customerResponse;
                }).toList();
    }

    private UserResponse getUserDetail(Customer customer) {
        UserResponse user=new UserResponse();
        user.setUsername(customer.getUser().getUsername());
        user.setId(customer.getUser().getId());
        user.setFirstname(customer.getUser().getFirstName());
        user.setLastname(customer.getUser().getLastName());
        user.setEmail(customer.getUser().getEmail());
        user.setPhone(customer.getUser().getPhone());
        user.setRoles(customer.getUser().getRole());
        user.setCreatedAt(customer.getUser().getCreatedAt());
        user.setUpdatedAt(customer.getUser().getUpdatedAt());
        return user;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = DETAILS_CACHE_NAME,key = "#id")
    @Override
    public ResponseEntity<DataResponse> findCustomerById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.map(customer -> {
            List<CustomerResponse> list=new ArrayList<>();
            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setId(customer.getId());
            customerResponse.setCustomer_number(customer.getCustomerNumber());
            UserResponse user=getUserDetail(customer);
            customerResponse.setUser(user);
            list.add(customerResponse);
            return new ResponseEntity<DataResponse>(
                    new DataResponse("success", true, list),
                    HttpStatus.OK
            );
        }).orElseGet(() -> new ResponseEntity<>(
                new DataResponse("Customer not found", false, null),
                HttpStatus.NOT_FOUND
        ));
    }

}
