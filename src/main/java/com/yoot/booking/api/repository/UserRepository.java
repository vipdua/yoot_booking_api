package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Role;
import com.yoot.booking.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndIsActiveTrue(String email);
    Optional<User> findFirstByRole(Role role);
    Optional<User> findFirstByRoleAndIdNot( Role role, Long id );
}