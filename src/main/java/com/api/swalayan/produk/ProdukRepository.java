package com.api.swalayan.produk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProdukRepository extends JpaRepository<Produk, Integer> {
    Page<Produk> findAll(Pageable pageable);
}
