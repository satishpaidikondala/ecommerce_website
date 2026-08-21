package com.ecommerce.user.service;

import java.util.List;

import com.ecommerce.common.entity.User;
import com.ecommerce.common.entity.UserRole;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getUsersByUserRoleAndActive(UserRole userRole, boolean active);

    User updateUser(Long id, User updatedUser);

    User deactivateUser(Long id, Long currentUserId);

    String forgotPassword(String email);

    void resetPassword(String token, String newPassword);
}
