package com.api.swalayan.order.Details;

import com.api.swalayan.order.Order;
import com.api.swalayan.produk.Produk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "tbl_order_items")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product")
    private Produk produk;

    private int quantity;
    private Double price;
    private Double total;
}

