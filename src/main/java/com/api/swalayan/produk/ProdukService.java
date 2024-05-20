package com.api.swalayan.produk;

import com.api.swalayan.qrcode.QrcodeService;
import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import com.api.swalayan.user.UserRepository;
import com.api.swalayan.util.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdukService implements ProdukInterface {

    private final ProdukRepository produkRepository;
    private final String CACHE_NAME="produk";

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = CACHE_NAME, key = "#pageNumber + '_' + #pageSize", condition = "#pageSize <= 100 and #pageSize > 0")
    public ResponseEntity<DataResponse> show(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC,"id");
        List<ProdukResponse> list=(pageSize<=100) ? loadFromCache(pageable) : loadFromDatabase(pageable);
        return new ResponseEntity<>(new DataResponse("success",true, list), HttpStatus.OK);
    }



    @SneakyThrows
    @Transactional
    @CacheEvict(value =CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> create(ProdukRequest produkRequest, MultipartFile multipartFile) {
        String sub="produk";
        Produk produk=ProdukMapper.toProduk(produkRequest);
        produk.setImage(UploadFile.Upload(multipartFile,sub));
        String qrCode= QrcodeService.generateQRCode("12345678",200,200,produkRequest.getName());
        produk.setQrQcode(produkRequest.getQrcode());
        produkRepository.save(produk);
        return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.CREATED);
    }

    @Transactional
    @CacheEvict(value =CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> update(int id, ProdukRequest produkRequest,MultipartFile multipartFile) {
        if(produkRepository.existsById(id)) {
            Produk produk=produkRepository.findById(id).orElseThrow();
            produk.setName(produkRequest.getName());
            produk.setDescription(produkRequest.getDescription());
            produk.setPrice(produkRequest.getPrice());
            produk.setPrice((produkRequest.getPrice()));
            produk.setStock(produkRequest.getStock());
            produk.setImage(produkRequest.getImage());
            produk.setQrQcode(produkRequest.getQrcode());
            produkRepository.save(produk);
            return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseApi("failed",false), HttpStatus.OK);
    }

    @Transactional
    @CacheEvict(value =CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> delete(int id) {
        if(produkRepository.existsById(id)) {
            produkRepository.deleteById(id);
            return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseApi("INVALID ID",false), HttpStatus.OK);
    }


    private List<ProdukResponse> loadFromDatabase(Pageable pageable) {
       Page<Produk> produks=produkRepository.findAll(pageable);
       return produks.stream().map(
               produk -> {
                 ProdukResponse produkResponse = new ProdukResponse();
                 produkResponse.setId(produk.getId());
                 produkResponse.setName(produk.getName());
                 produkResponse.setDescription(produk.getDescription());
                 produkResponse.setPrice(produk.getPrice());
                 produkResponse.setStock(produk.getStock());
                 produkResponse.setImage(produk.getImage());
                 produkResponse.setQrcode(produk.getQrQcode());
                 return produkResponse;
               }).toList();
    }

    private List<ProdukResponse> loadFromCache(Pageable pageable) {
        return loadFromDatabase(pageable);
    }
}
