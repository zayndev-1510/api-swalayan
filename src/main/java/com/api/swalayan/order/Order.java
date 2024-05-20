package com.api.swalayan.order;

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
    @Column(name = "customer")
    private Integer customerId;
    @Column(name = "product")
    private Integer productId;
    private Integer quantity;
    private Double price;
    private Double total;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(nullable = false,updatable = true)
    private Timestamp updatedAt;

}
