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
   @PostMapping
    public ResponseEntity<ResponseApi> addProduk(@Valid @RequestParam("foto") MultipartFile foto,@ModelAttribute ProdukRequest produkRequest) {
        return produkService.create(produkRequest,foto);
    }

    @PostMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseApi> updateProduk(@PathVariable("id") Integer id, @RequestBody ProdukRequest  produkRequest,@RequestParam("file") MultipartFile file) {
        return produkService.update(id,produkRequest,file);
    }

    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseApi> deleteProduk(@PathVariable("id") Integer id) {
        return produkService.delete(id);
    }

}
