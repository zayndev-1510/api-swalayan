package com.api.swalayan.produk;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/produk/")
@RequiredArgsConstructor
public class ProdukController {
    private final ProdukService produkService;

    @GetMapping(value = "{a}/{b}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> getProduk(@PathVariable("a") Integer a, @PathVariable("b") Integer b) {
        return produkService.show(a,b);
    }

    @GetMapping(value = "/{product}/{a}/{b}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> filterProduct(@PathVariable("product") String product,@PathVariable("a") Integer a, @PathVariable("b") Integer b) {
        return produkService.filterProduct(product,a,b);
    }
    @GetMapping(value = "{sort}/{keyword}/{a}/{b}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse> getProdukBySort(@PathVariable("sort") String sort,@PathVariable("keyword") String keyword,@PathVariable("a") Integer a, @PathVariable("b") Integer b) {
        return produkService.sortProduct(sort,keyword,a,b);
    }

   @PostMapping
    public ResponseEntity<ResponseApi> addProduk(@Valid @RequestParam("foto") MultipartFile foto,@ModelAttribute ProdukRequest produkRequest) {
        return produkService.create(produkRequest,foto);
    }

    @PostMapping("{id}")
    public ResponseEntity<ResponseApi> updateProduk(@PathVariable("id") Integer id,@ModelAttribute ProdukRequest produkRequest,@RequestParam("foto") MultipartFile file) {
        return produkService.update(id,produkRequest,file);
    }

    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseApi> deleteProduk(@PathVariable("id") Integer id) {
        return produkService.delete(id);
    }

}
