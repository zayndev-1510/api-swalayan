package com.api.swalayan.order;

import com.api.swalayan.customer.Customer;
import com.api.swalayan.customer.CustomerMapper;
import com.api.swalayan.customer.CustomerResponse;
import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import com.api.swalayan.user.UserMapper;
import com.api.swalayan.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderInterface {
    private final OrderRepository orderRepository;
    private final String CACHE_NAME = "orders";

    @Cacheable(value = CACHE_NAME, key = "#page + '_' + #size", condition = "#page <= 100 and #size > 0")
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<DataResponse> showOrder(int page, int size) {
        Pageable pageable= PageRequest.of(page,size, Sort.Direction.DESC,"id");
        List<OrderResponse> orders=(size<=100) ? loadFromCache(pageable) : loadFromDatabase(pageable);
        return new ResponseEntity<>(new DataResponse("success",true,orders), HttpStatus.OK);
    }

    private List<OrderResponse> loadFromDatabase(Pageable pageable) {
        Page<Order> orders=orderRepository.findAllWithCustomer(pageable);
        return orders.stream().map(
                order -> {
                    OrderResponse orderResponse=new OrderResponse();
                    orderResponse.setOrder(order.getId());
                    Customer customer=order.getCustomer();
                    CustomerResponse customerResponse=new CustomerResponse();
                    customerResponse.setId(customer.getId());
                    customerResponse.setCustomer_number(customer.getCustomerNumber());

                    customerResponse.setUser(getUserDetail(customer));
                    orderResponse.setCustomer(customerResponse);
                    return orderResponse;
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
    private List<OrderResponse> loadFromCache(Pageable pageable) {
        return loadFromDatabase(pageable);
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> createOrder(OrderRequest orderRequest) {
        Order order= OrderMapper.toOrder(orderRequest);
        orderRepository.save(order);
        return new ResponseEntity<>(new ResponseApi("success",true),HttpStatus.CREATED);
    }
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> deleteOrder(int orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderRepository.delete(order);
                    return new ResponseEntity<>(new ResponseApi("success", true), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(new ResponseApi("error", false), HttpStatus.NOT_FOUND));
    }
}
