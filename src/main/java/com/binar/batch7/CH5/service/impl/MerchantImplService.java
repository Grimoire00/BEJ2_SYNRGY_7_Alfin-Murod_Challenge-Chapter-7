package com.binar.batch7.CH5.service.impl;

import com.binar.batch7.CH5.dto.MerchantRequest;
import com.binar.batch7.CH5.dto.MerchantResponse;
import com.binar.batch7.CH5.mapper.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import com.binar.batch7.CH5.entity.Merchant;
import com.binar.batch7.CH5.repository.MerchantRepo;
import com.binar.batch7.CH5.service.MerchantService;
import com.binar.batch7.CH5.service.ValidasiService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MerchantImplService implements MerchantService {

    @Autowired
    private MerchantRepo merchantRepository;

    @Autowired
    private ValidasiService validationService;

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public MerchantResponse create(MerchantRequest request) {
        validationService.validate(request);
        Merchant merchant = new Merchant();
        var randomUUID = UUID.randomUUID();
        merchant.setId(randomUUID);
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setIsOpen(request.getIsOpen());
        merchantRepository.createQuerySP(randomUUID, request.getName(), request.getLocation(), request.getIsOpen());
        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public List<MerchantResponse> findAll(Boolean isOpen) {
        var response = new ArrayList<MerchantResponse>();
        if (isOpen != null) {
            merchantRepository.findAllByIsOpen(isOpen).forEach(
                    merchant -> response.add(merchantMapper.toMerchantResponse(merchant))
            );
        } else {
            merchantRepository.findAll().forEach(
                    merchant -> response.add(merchantMapper.toMerchantResponse(merchant))
            );
        }
        return response;
    }

    @Override
    public MerchantResponse update(UUID id, MerchantRequest request) {
        validationService.validate(request);
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Merchant not found"));
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setIsOpen(request.getIsOpen());
        merchantRepository.updateQuerySP(id, request.getName(), request.getLocation(), request.getIsOpen());
        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public MerchantResponse delete(UUID id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Merchant not found"));
        merchantRepository.deleteQuerySP(merchant.getId());
        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public MerchantResponse findById(UUID id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Merchant not found"));
        merchantRepository.findByIdQuerySP(merchant.getId());
        return merchantMapper.toMerchantResponse(merchant);
    }

    private MerchantResponse getMerchantResponse(MerchantRequest request, Merchant merchant) {
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setIsOpen(request.getIsOpen());
        merchantRepository.save(merchant);
        return merchantMapper.toMerchantResponse(merchant);
    }

}
