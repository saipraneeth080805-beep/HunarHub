package com.hunarhub.repository;

import com.hunarhub.entity.Review;
import com.hunarhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEntrepreneur(User entrepreneur);
    List<Review> findByCustomer(User customer);
    boolean existsByCustomerAndEntrepreneur(User customer, User entrepreneur);
}
