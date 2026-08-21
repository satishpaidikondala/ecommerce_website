package com.ecommerce.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.User;
import com.ecommerce.common.entity.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find by email (might not exist)
    Optional<User> findByEmail(String email);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find by role and active status
    List<User> findByUserRoleAndActive(UserRole userRole, boolean active);

    // Find by reset token
    Optional<User> findByResetToken(String resetToken);

    // Search by name
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
}