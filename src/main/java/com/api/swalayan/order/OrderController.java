package com.api.swalayan.order;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping(value = "/{a}/{b}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> getOrder(@PathVariable("a") int a, @PathVariable("b") int b) {
        return orderService.showOrder(a,b);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,value = "/")
    public ResponseEntity<ResponseApi> createOrder(@Valid  @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/{id}")
    public ResponseEntity<ResponseApi> deleteOrder(@PathVariable("id") int id) {
        return orderService.deleteOrder(id);
    }
}
