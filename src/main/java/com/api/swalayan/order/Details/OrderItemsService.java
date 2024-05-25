package com.api.swalayan.order.Details;

import com.api.swalayan.customer.CustomerResponse;
import com.api.swalayan.order.Order;

import com.api.swalayan.order.OrderRepository;
import com.api.swalayan.order.OrderResponse;
import com.api.swalayan.produk.Produk;
import com.api.swalayan.produk.ProdukResponse;
import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
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
public class OrderItemsService implements OrderInterface{

    private final OrderItemsRepository orderItemsRepository;
    private final String CACHE_NAME="order_items";
    @Cacheable(value = CACHE_NAME, key = "#page + '_' + #size", condition = "#page <= 100 and #size > 0")
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<DataResponse> getOrder(int page, int size) {
        Pageable pageable= PageRequest.of(page,size, Sort.Direction.DESC,"id");
        List<OrderItemsResponse> list=(size<=100) ? loadFromCache(pageable) : loadFromDatabase(pageable);
        return new ResponseEntity<>(new DataResponse("success",true,list), HttpStatus.OK);
    }

    private List<OrderItemsResponse> loadFromDatabase(Pageable pageable) {
        Page<OrderItems> orderItems=orderItemsRepository.findAllWithProdukAndOrder(pageable);
        return orderItems.stream().map(
                orderitem->{
                    OrderItemsResponse orderItemsResponse=new OrderItemsResponse();
                    orderItemsResponse.setOrderitem(orderitem.getId());
                    ProdukResponse produkResponse=new ProdukResponse();
                    Produk produk=orderitem.getProduk();
                    produkResponse.setId(produk.getId());
                    produkResponse.setName(produk.getName());
                    produkResponse.setPrice(produk.getPrice());
                    orderItemsResponse.setProduk(produkResponse);
                    Order order=orderitem.getOrder();
                    OrderResponse orderResponse=new OrderResponse();
                    orderResponse.setOrder(order.getId());
                    CustomerResponse customerResponse=new CustomerResponse();
                    customerResponse.setId(order.getCustomer().getId());
                    customerResponse.setCustomer_number(order.getCustomer().getCustomerNumber());
                    orderItemsResponse.setOrder(orderResponse);
                    orderResponse.setCustomer(customerResponse);
                    orderItemsResponse.setQuantity(orderitem.getQuantity());
                    orderItemsResponse.setPrice(orderitem.getPrice());
                    orderItemsResponse.setTotal(orderitem.getTotal());

                    return orderItemsResponse;
                }).toList();
    }

    private List<OrderItemsResponse> loadFromCache(Pageable pageable) {
        return loadFromDatabase(pageable);
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Transactional
    @Override
    public ResponseEntity<ResponseApi> createOrder(OrderItemsRequest order) {

        OrderItems orderItems=OrderItemMapper.toOrderItem(order);
        orderItemsRepository.save(orderItems);
        return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.OK);
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Transactional
    @Override
    public ResponseEntity<ResponseApi> updateOrder(OrderItemsRequest order, Long id) {
        return orderItemsRepository.findById(id)
                .map(existingOrderItem -> { // Optional Handling
                    existingOrderItem.setQuantity(order.getQuantity());
                    Produk produk=new Produk();
                    existingOrderItem.setPrice(produk.getPrice());
                    existingOrderItem.setTotal(order.getQuantity() * produk.getPrice());
                    orderItemsRepository.save(existingOrderItem);
                    return new ResponseEntity<>(new ResponseApi("success", true), HttpStatus.OK);
                })
                .orElseGet(() -> { // If order item not found
                    return new ResponseEntity<>(new ResponseApi("Order item not found", false), HttpStatus.NOT_FOUND);
                });
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Transactional
    @Override
    public ResponseEntity<ResponseApi> deleteOrder(Long id) {
        return orderItemsRepository.findById(id)
                .map(
                        orderItems -> {
                            orderItemsRepository.delete(orderItems);
                            return new ResponseEntity<>(new ResponseApi("success", true), HttpStatus.OK);
                        })
                .orElseGet(()-> new ResponseEntity<>(new ResponseApi("Order item not found", false), HttpStatus.NOT_FOUND));
    }
}
