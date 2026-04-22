package com.hunarhub.controller;

import com.hunarhub.dto.*;
import com.hunarhub.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/entrepreneurs")
    public ResponseEntity<?> getAllEntrepreneurs(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search) {
        if (page != null && size != null) {
            return ResponseEntity.ok(customerService.getAllEntrepreneurs(page, size, search));
        }
        return ResponseEntity.ok(customerService.getAllEntrepreneurs());
    }

    @GetMapping("/entrepreneurs/{id}")
    public ResponseEntity<EntrepreneurDto> getEntrepreneurById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getEntrepreneurById(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(customerService.getCategories());
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search) {
        if (page != null && size != null) {
            return ResponseEntity.ok(customerService.getAllProducts(page, size, categoryId, minPrice, maxPrice, search));
        }
        return ResponseEntity.ok(customerService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getProductById(id));
    }

    @GetMapping("/entrepreneurs/{id}/products")
    public ResponseEntity<List<ProductDto>> getProductsByEntrepreneur(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getProductsByEntrepreneur(id));
    }

    @GetMapping("/entrepreneurs/{id}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsByEntrepreneur(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getReviewsByEntrepreneur(id));
    }

    @PostMapping("/requests")
    public ResponseEntity<ServiceRequestDto> createServiceRequest(
            @RequestBody Map<String, Object> request) {
        Long entrepreneurId = Long.valueOf(request.get("entrepreneurId").toString());
        String serviceDescription = request.get("serviceDescription").toString();
        return ResponseEntity.ok(customerService.createServiceRequest(entrepreneurId, serviceDescription));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Long productId = Long.valueOf(orderRequest.get("productId").toString());
        Integer quantity = Integer.valueOf(orderRequest.get("quantity").toString());
        return ResponseEntity.ok(customerService.createOrder(productId, quantity));
    }

    @GetMapping("/orders/my")
    public ResponseEntity<?> getMyOrders(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(customerService.getMyOrders(page, size));
        }
        return ResponseEntity.ok(customerService.getMyOrders());
    }

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.cancelOrder(id));
    }

    @GetMapping("/requests/my")
    public ResponseEntity<?> getMyServiceRequests(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(customerService.getMyServiceRequests(page, size));
        }
        return ResponseEntity.ok(customerService.getMyServiceRequests());
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDto> createReview(@RequestBody Map<String, Object> reviewRequest) {
        Long entrepreneurId = Long.valueOf(reviewRequest.get("entrepreneurId").toString());
        Integer rating = Integer.valueOf(reviewRequest.get("rating").toString());
        String comment = reviewRequest.get("comment") != null ? reviewRequest.get("comment").toString() : "";
        return ResponseEntity.ok(customerService.createReview(entrepreneurId, rating, comment));
    }

    @PostMapping("/disputes")
    public ResponseEntity<DisputeDto> createDispute(@Valid @RequestBody CreateDisputeRequest request) {
        return ResponseEntity.ok(customerService.createDispute(request));
    }

    @GetMapping("/disputes/my")
    public ResponseEntity<List<DisputeDto>> getMyDisputes() {
        return ResponseEntity.ok(customerService.getMyDisputes());
    }

    @GetMapping("/disputes/against-me")
    public ResponseEntity<List<DisputeDto>> getDisputesAgainstMe() {
        return ResponseEntity.ok(customerService.getDisputesAgainstMe());
    }
}
