package com.api.swalayan.order.Details;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order-items")
public class OrderItemsController {
    private final OrderItemsService orderItemsService;

    @GetMapping(value = "/{a}/{b}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> getOrderItems(@PathVariable("a") int a, @PathVariable("b") int b){
        return orderItemsService.getOrder(a,b);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseApi> addOrderItems(@Valid @RequestBody OrderItemsRequest orderItems){
        return orderItemsService.createOrder(orderItems);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseApi> updateOrderItems( @Valid @PathVariable("id") Long id, @RequestBody OrderItemsRequest orderItems){
        return orderItemsService.updateOrder(orderItems,id);
    }

    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseApi> deleteOrderItems(@PathVariable("id") Long id){
        return orderItemsService.deleteOrder(id);
    }
}
