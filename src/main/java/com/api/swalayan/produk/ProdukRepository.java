package com.api.swalayan.produk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProdukRepository extends JpaRepository<Produk, Integer> {
    Page<Produk> findAll(Pageable pageable);

    @Query("SELECT p FROM Produk p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :product, '%'))")
    Page<Produk> searchByNameIgnoreCase(String product, Pageable pageable);


}
