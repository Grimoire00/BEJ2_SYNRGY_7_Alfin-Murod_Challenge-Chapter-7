package com.binar.batch7.CH5.service;

import com.binar.batch7.CH5.dto.UserRequest;
import com.binar.batch7.CH5.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest userRequest);

    List<UserResponse> findAll();

    UserResponse update(UUID id, UserRequest request);

    UserResponse delete(UUID id);

    UserResponse findById(UUID id);
}