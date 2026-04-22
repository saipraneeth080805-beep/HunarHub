package com.hunarhub.repository;

import com.hunarhub.entity.ServiceRequest;
import com.hunarhub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByCustomer(User customer);
    Page<ServiceRequest> findByCustomer(User customer, Pageable pageable);
    List<ServiceRequest> findByEntrepreneur(User entrepreneur);
    Page<ServiceRequest> findByEntrepreneur(User entrepreneur, Pageable pageable);
    List<ServiceRequest> findByEntrepreneurAndStatus(User entrepreneur, ServiceRequest.RequestStatus status);
    Page<ServiceRequest> findByEntrepreneurAndStatus(User entrepreneur, ServiceRequest.RequestStatus status, Pageable pageable);
    long countByStatus(ServiceRequest.RequestStatus status);
}
