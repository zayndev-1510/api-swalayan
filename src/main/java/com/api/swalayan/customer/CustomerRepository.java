package com.api.swalayan.customer;

import com.api.swalayan.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findFirstByCustomerNumberStartingWithOrderByCustomerNumberDesc(String yearMonth);
    @Query(value = "SELECT c from Customer c JOIN FETCH c.user")
    Page<Customer> findAllWithUser(Pageable pageable);
    void deleteByUser(User user);
}
