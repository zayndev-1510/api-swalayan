package com.api.swalayan.order.Details;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    @Query(value = "SELECT oi FROM OrderItems oi")
    @EntityGraph(attributePaths = {"produk", "order", "order.customer"}) // Sesuaikan dengan relasi Anda
    Page<OrderItems> findAllWithProdukAndOrder(Pageable pageable);
}
