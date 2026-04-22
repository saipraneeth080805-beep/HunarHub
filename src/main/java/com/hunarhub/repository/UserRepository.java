package com.hunarhub.repository;

import com.hunarhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(User.Role role);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findFirstByNameIgnoreCase(String name);
    boolean existsByEmail(String email);
    long countByRole(User.Role role);
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}
