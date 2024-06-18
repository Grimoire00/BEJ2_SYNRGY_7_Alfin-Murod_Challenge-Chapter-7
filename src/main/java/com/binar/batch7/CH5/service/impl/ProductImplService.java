package com.binar.batch7.CH5.service.impl;

import com.binar.batch7.CH5.dto.ProductRequest;
import com.binar.batch7.CH5.dto.ProductResponse;
import com.binar.batch7.CH5.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import com.binar.batch7.CH5.entity.Merchant;
import com.binar.batch7.CH5.entity.Product;
import com.binar.batch7.CH5.repository.MerchantRepo;
import com.binar.batch7.CH5.repository.ProductRepo;
import com.binar.batch7.CH5.service.ProductService;
import com.binar.batch7.CH5.service.ValidasiService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class ProductImplService implements ProductService {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private MerchantRepo merchantRepository;

    @Autowired
    private ValidasiService validationService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        validationService.validate(request);
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setName(request.getName());
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found with id " + request.getMerchantId()));
        product.setMerchant(merchant);
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public List<ProductResponse> findAll() {
        var response = new ArrayList<ProductResponse>();
        productRepository.findAll().forEach(
                product -> {
                    log.info("PRODUCT : {}", product);
                    response.add(productMapper.toProductResponse(product));
                }
        );
        return response;
    }

    @Override
    public ProductResponse update(UUID id, ProductRequest request) {
        validationService.validate(request);
        log.info("REQUEST : {}", request);
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Product not found"));

        if (Objects.nonNull(request.getName())) {
            product.setName(request.getName());
        }

        if (Objects.nonNull(request.getPrice())) {
            product.setPrice(request.getPrice());
        }

        product.setMerchant(product.getMerchant());

        productRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse delete(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Product not found"));
        productRepository.delete(product);
        return productMapper.toProductResponse(product);
    }
}