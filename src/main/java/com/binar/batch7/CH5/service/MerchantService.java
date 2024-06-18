package com.binar.batch7.CH5.service;

import com.binar.batch7.CH5.dto.MerchantRequest;
import com.binar.batch7.CH5.dto.MerchantResponse;

import java.util.List;
import java.util.UUID;

public interface MerchantService {

    MerchantResponse create(MerchantRequest request);

    List<MerchantResponse> findAll(Boolean isOpen);

    MerchantResponse update(UUID id, MerchantRequest request);

    MerchantResponse delete(UUID id);

    MerchantResponse findById(UUID id);

}
