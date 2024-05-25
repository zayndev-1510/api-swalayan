package com.api.swalayan.order.Details;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import org.springframework.http.ResponseEntity;

public interface OrderInterface {
    ResponseEntity<DataResponse> getOrder(int page,int size);
    ResponseEntity<ResponseApi> createOrder(OrderItemsRequest order);
    ResponseEntity<ResponseApi> updateOrder(OrderItemsRequest order,Long id);
    ResponseEntity<ResponseApi> deleteOrder(Long id);
}
