package com.api.swalayan.order;

import com.api.swalayan.customer.Customer;
import com.api.swalayan.produk.Produk;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "tbl_order")
@Entity
@Data
@NoArgsConstructor
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    private Double total;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;

}
