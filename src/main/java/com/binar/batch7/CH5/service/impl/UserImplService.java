package com.binar.batch7.CH5.service.impl;

import com.binar.batch7.CH5.dto.UserRequest;
import com.binar.batch7.CH5.dto.UserResponse;
import com.binar.batch7.CH5.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import com.binar.batch7.CH5.entity.User;
import com.binar.batch7.CH5.repository.UserRepo;
import com.binar.batch7.CH5.service.UserService;
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
public class UserImplService implements UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ValidasiService validationService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserResponse create(UserRequest userRequest) {
        validationService.validate(userRequest);
        User user = new User();
        var randomUUID = UUID.randomUUID();
        user.setId(randomUUID);
        user.setUsername(userRequest.getUsername());
        user.setEmailAddress(userRequest.getEmailAddress());
        user.setPassword(userRequest.getPassword());

        if (userRepository.existsByEmailAddress(userRequest.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }
        userRepository.createQuerySP(randomUUID, user.getUsername(), user.getEmailAddress(), user.getPassword());

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        var response = new ArrayList<UserResponse>();
        userRepository.findAll().forEach(user -> {
            log.info("USER : {}", user);
            response.add(userMapper.toUserResponse(user));
        });
        return response;
    }

    @Override
    public UserResponse update(UUID id, UserRequest request) {
        validationService.validate(request);
        log.info("REQUEST : {}", request);
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID User not found"));

        if (userRepository.existsByEmailAddress(request.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        if (Objects.nonNull(request.getUsername())) {
            user.setUsername(request.getUsername());
        }

        if (Objects.nonNull(request.getEmailAddress())) {
            user.setEmailAddress(request.getEmailAddress());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(request.getPassword());
        }

        userRepository.updateQuerySP(id, user.getUsername(), user.getEmailAddress(), user.getPassword());

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID User not found"));
        userRepository.deleteQuerySP(user.getId());

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID User not found"));
        userRepository.findByIdQuerySP(user.getId());
        return userMapper.toUserResponse(user);
    }

}
