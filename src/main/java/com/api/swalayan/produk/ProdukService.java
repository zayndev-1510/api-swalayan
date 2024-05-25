package com.api.swalayan.produk;

import com.api.swalayan.qrcode.QrcodeService;
import com.api.swalayan.response.DataResponse;
import com.api.swalayan.response.ResponseApi;
import com.api.swalayan.util.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

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
        Page<Produk> produkPage = produkRepository.findAll(pageable);
        List<ProdukResponse> list=(pageSize<=100) ? loadFromCache(produkPage) : loadFromDatabase(produkPage);
        return new ResponseEntity<>(new DataResponse("success",true, list,produkPage.getTotalElements(),produkPage.getTotalPages(),pageNumber,pageSize), HttpStatus.OK);
    }

    @SneakyThrows
    @Transactional
    @CacheEvict(value =CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> create(ProdukRequest produkRequest, MultipartFile multipartFile) {
        String sub="produk";
        Produk produk=ProdukMapper.toProduk(produkRequest);
        String image=UploadFile.Upload(multipartFile,sub);
        produk.setImage(Objects.requireNonNullElse(image, "not image"));
        String qrCode= QrcodeService.generateQRCode("12345678",200,200,produkRequest.getQrcode());
        produk.setQrQcode(qrCode);
        produkRepository.save(produk);
        return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.CREATED);
    }

    @SneakyThrows
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
            String sub="produk";
            String image=UploadFile.Upload(multipartFile,sub);
            if(image==null){
                produk.setImage(produk.getImage());
            }else{
                produk.setImage(image);
            }

            if(produkRequest.getQrcode().equals(produk.getQrQcode())){
                produk.setQrQcode(produk.getQrQcode());
            }else{
                String qrCode= QrcodeService.generateQRCode("12345678",200,200,produkRequest.getQrcode());
                produk.setQrQcode(qrCode);
            }

            produkRepository.save(produk);
            return new ResponseEntity<>(new ResponseApi("success",true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseApi("failed",false), HttpStatus.OK);
    }

    @Transactional
    @CacheEvict(value =CACHE_NAME, allEntries = true)
    @Override
    public ResponseEntity<ResponseApi> delete(int id) {
        return produkRepository.findById(id)
                .map(product -> {

                    produkRepository.delete(product);
                    return new ResponseEntity<>(new ResponseApi("success", true), HttpStatus.OK);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));
    }

    @Override
    public ResponseEntity<DataResponse> filterProduct(String product, int pagenumber, int pagesize) {
        Pageable pageable = PageRequest.of(pagenumber, pagesize, Sort.Direction.DESC,"id");
        Page<Produk> produkPage = produkRepository.searchByNameIgnoreCase(product,pageable);
        List<ProdukResponse> list=loadFromCache(produkPage);
        return new ResponseEntity<>(new DataResponse("success",true, list,produkPage.getTotalElements(),produkPage.getTotalPages(),pagenumber,pagesize), HttpStatus.OK);

    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = CACHE_NAME, key = "#pageNumber + '_' + #pageSize+'_'+#sort+'_'+#keyword", condition = "#pageSize <= 100 and #pageSize > 0")
    public ResponseEntity<DataResponse> sortProduct(String sort, String keyword, int pageNumber, int pageSize) {
        Sort.Direction sortDirection = Sort.Direction.fromString(sort.toUpperCase());
        Pageable page = PageRequest.of(pageNumber, pageSize, sortDirection,keyword);
        Page<Produk> produkPage = produkRepository.findAll(page);
        List<ProdukResponse> list=(pageSize<=100) ? loadFromCache(produkPage) : loadFromDatabase(produkPage);
        return new ResponseEntity<>(new DataResponse("success",true, list,produkPage.getTotalElements(),produkPage.getTotalPages(),pageNumber,pageSize), HttpStatus.OK);
    }


    private List<ProdukResponse> loadFromDatabase(Page<Produk> products) {
       return products.stream().map(
               produk -> {
                   ProdukResponse produkResponse = new ProdukResponse();
                   produkResponse.setId(produk.getId());
                   produkResponse.setName(produk.getName());
                   produkResponse.setDescription(produk.getDescription());
                   produkResponse.setPrice(produk.getPrice());
                   produkResponse.setStock(produk.getStock());
                   produkResponse.setImage(produk.getImage());
                   String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                           .path("/preview/"+produk.getQrQcode()).toUriString();

                   produkResponse.setQrcode(imageUrl);
                   return produkResponse;
               }).toList();
    }

    private List<ProdukResponse> loadFromCache(Page<Produk> products) {
        return loadFromDatabase(products);
    }
}
