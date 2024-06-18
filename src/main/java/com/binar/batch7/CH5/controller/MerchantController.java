package com.binar.batch7.CH5.controller;

import com.binar.batch7.CH5.dto.BaseResponse;
import com.binar.batch7.CH5.dto.MerchantRequest;
import com.binar.batch7.CH5.service.MerchantService;
import com.binar.batch7.CH5.sp.QuerySPMerchant;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
//@Hidden
@RequestMapping("api/v1/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private QuerySPMerchant merchantQuerySP;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MerchantRequest merchantRequest) {
        jdbcTemplate.execute(merchantQuerySP.create);
        return ResponseEntity.ok(BaseResponse.success(merchantService.create(merchantRequest), "Success Create Merchant"));
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) Boolean isOpen) {
        return ResponseEntity.ok(BaseResponse.success(merchantService.findAll(isOpen), "Success Get All Merchants"));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        jdbcTemplate.execute(merchantQuerySP.readById);
        return ResponseEntity.ok(BaseResponse.success(merchantService.findById(id), "Success Get Detail Merchant"));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody MerchantRequest merchantRequest) {
        jdbcTemplate.execute(merchantQuerySP.update);
        return ResponseEntity.ok(BaseResponse.success(merchantService.update(id, merchantRequest), "Success Update Merchant"));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        jdbcTemplate.execute(merchantQuerySP.delete);
        return ResponseEntity.ok(BaseResponse.success(merchantService.delete(id), "Success Delete Merchant"));
    }
}
