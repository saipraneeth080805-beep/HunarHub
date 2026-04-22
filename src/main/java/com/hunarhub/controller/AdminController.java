package com.hunarhub.controller;

import com.hunarhub.dto.*;
import com.hunarhub.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    @Autowired
    private AdminService adminService;

    // Entrepreneur Management
    @GetMapping("/entrepreneurs/pending")
    public ResponseEntity<?> getPendingEntrepreneurs(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(adminService.getPendingEntrepreneurs(page, size));
        }
        return ResponseEntity.ok(adminService.getPendingEntrepreneurs());
    }

    @GetMapping("/entrepreneurs")
    public ResponseEntity<List<EntrepreneurDto>> getAllEntrepreneurs() {
        return ResponseEntity.ok(adminService.getAllEntrepreneurs());
    }

    @PutMapping("/entrepreneurs/{id}/approve")
    public ResponseEntity<EntrepreneurDto> approveEntrepreneur(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.approveEntrepreneur(id));
    }

    @PutMapping("/entrepreneurs/{id}/reject")
    public ResponseEntity<EntrepreneurDto> rejectEntrepreneur(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.rejectEntrepreneur(id));
    }

    // Order Management
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(adminService.getAllOrders(page, size));
        }
        return ResponseEntity.ok(adminService.getAllOrders());
    }

    // Service Request Management
    @GetMapping("/requests")
    public ResponseEntity<?> getAllServiceRequests(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(adminService.getAllServiceRequests(page, size));
        }
        return ResponseEntity.ok(adminService.getAllServiceRequests());
    }

    // Category Management
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(adminService.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(adminService.createCategory(request));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(adminService.updateCategory(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
    }

    // Dispute Management
    @GetMapping("/disputes")
    public ResponseEntity<List<DisputeDto>> getAllDisputes(
            @RequestParam(required = false) String status) {
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(adminService.getDisputesByStatus(status));
        }
        return ResponseEntity.ok(adminService.getAllDisputes());
    }

    @GetMapping("/disputes/{id}")
    public ResponseEntity<DisputeDto> getDisputeById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getDisputeById(id));
    }

    @PutMapping("/disputes/{id}/status")
    public ResponseEntity<DisputeDto> updateDisputeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        String adminResponse = request.get("adminResponse");
        return ResponseEntity.ok(adminService.updateDisputeStatus(id, status, adminResponse));
    }

    // Analytics and Reports
    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        return ResponseEntity.ok(adminService.getAnalytics());
    }

    // Users listing
    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
