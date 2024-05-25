package com.api.swalayan.order;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import org.springframework.http.ResponseEntity;

public interface OrderInterface {
    ResponseEntity<DataResponse> showOrder(int page,int size);
    ResponseEntity<ResponseApi> createOrder(OrderRequest orderRequest);
    ResponseEntity<ResponseApi> deleteOrder(int orderId);
}
