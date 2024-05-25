package com.api.swalayan.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.customer c " +
            "JOIN FETCH c.user u")
    Page<Order> findAllWithCustomer(Pageable pageable);
}
