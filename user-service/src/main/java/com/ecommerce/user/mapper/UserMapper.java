package com.ecommerce.user.mapper;

import com.ecommerce.common.entity.User;
import com.ecommerce.user.dto.UserResponse;

public class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getUserRole() != null ? user.getUserRole().name() : null,
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
