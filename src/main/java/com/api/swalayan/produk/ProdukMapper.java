package com.api.swalayan.produk;

public class ProdukMapper {
    public static Produk toProduk(ProdukRequest produkRequest) {
        Produk produk = new Produk();
        produk.setName(produkRequest.getName());
        produk.setDescription(produkRequest.getDescription());
        produk.setPrice(produkRequest.getPrice());
        produk.setStock(produkRequest.getStock());
        return produk;
    }
}
