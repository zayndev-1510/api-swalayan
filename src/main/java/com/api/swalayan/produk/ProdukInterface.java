package com.api.swalayan.produk;

import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProdukInterface {
    ResponseEntity<DataResponse> show(int pagenumber, int pagesize);
    ResponseEntity<ResponseApi> create(ProdukRequest produkRequest, MultipartFile multipartFile);
    ResponseEntity<ResponseApi> update(int id, ProdukRequest produkRequest,MultipartFile multipartFile);
    ResponseEntity<ResponseApi> delete(int id);
}
