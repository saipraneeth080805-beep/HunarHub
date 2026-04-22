package com.hunarhub.repository;

import com.hunarhub.entity.Order;
import com.hunarhub.entity.User;
import com.hunarhub.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User customer);
    Page<Order> findByCustomer(User customer, Pageable pageable);
    List<Order> findByProductEntrepreneur(User entrepreneur);
    Page<Order> findByProductEntrepreneur(User entrepreneur, Pageable pageable);
    void deleteByProduct(Product product);
    long countByStatus(Order.OrderStatus status);
}
