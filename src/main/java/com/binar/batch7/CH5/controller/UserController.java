package com.binar.batch7.CH5.controller;

import com.binar.batch7.CH5.dto.BaseResponse;
import com.binar.batch7.CH5.dto.UserRequest;
import com.binar.batch7.CH5.service.UserService;
import com.binar.batch7.CH5.sp.QuerySPUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuerySPUser userQuerySP;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UserRequest userRequest) {
        jdbcTemplate.execute(userQuerySP.create);
        return ResponseEntity.ok(BaseResponse.success(userService.create(userRequest), "Success Create User"));
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(BaseResponse.success(userService.findAll(), "Success Get All Users"));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        jdbcTemplate.execute(userQuerySP.update);
        return ResponseEntity.ok(BaseResponse.success(userService.update(id, request), "Success Update User"));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        jdbcTemplate.execute(userQuerySP.delete);
        return ResponseEntity.ok(BaseResponse.success(userService.delete(id), "Success Delete User"));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        jdbcTemplate.execute(userQuerySP.readById);
        return ResponseEntity.ok(BaseResponse.success(userService.findById(id), "Success Get Detail User"));
    }
}
