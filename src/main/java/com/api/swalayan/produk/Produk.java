package com.api.swalayan.produk;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Table(name = "tbl_produk")
@Entity
@Data
public class Produk implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,length = 100)
    private String name;
    @Column(nullable = false,length = 100)
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private String image;
    private String qrQcode;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(nullable = false,updatable = true)
    private Timestamp updatedAt;
}
