package com.api.swalayan.order.Details;

import com.api.swalayan.order.Order;
import com.api.swalayan.produk.Produk;

public class OrderItemMapper {
    public static OrderItems toOrderItem(OrderItemsRequest orderItemsRequest) {
        OrderItems orderItems = new OrderItems();
        Order order = new Order();
        order.setId(orderItemsRequest.getOrder());
        Produk produk=new Produk();
        produk.setId(orderItemsRequest.getProduk());

        orderItems.setOrder(order);
        orderItems.setProduk(produk);

        orderItems.setQuantity(orderItemsRequest.getQuantity());
        orderItems.setPrice(orderItemsRequest.getPrice());
        Double total= orderItemsRequest.getPrice()*orderItemsRequest.getQuantity();
        orderItems.setTotal(total);
        return orderItems;
    }
}
