package com.binar.batch7.CH5.mapper;


import com.binar.batch7.CH5.dto.UserResponse;
import com.binar.batch7.CH5.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .emailAddress(user.getEmailAddress())
                .build();
    }
}