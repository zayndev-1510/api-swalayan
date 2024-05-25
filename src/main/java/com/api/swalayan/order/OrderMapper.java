package com.api.swalayan.order;

import com.api.swalayan.customer.Customer;

public class OrderMapper {
    public static Order toOrder(OrderRequest orderRequest) {
        Order order = new Order();
        Customer customer = new Customer();
        customer.setId(orderRequest.getCustomer());
        order.setCustomer(customer);
        order.setTotal(0.00);
        return order;
    }
}
