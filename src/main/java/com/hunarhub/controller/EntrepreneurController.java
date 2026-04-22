package com.hunarhub.controller;

import com.hunarhub.dto.*;
import com.hunarhub.service.EntrepreneurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entrepreneur")
@CrossOrigin(origins = "http://localhost:3000")
public class EntrepreneurController {
    @Autowired
    private EntrepreneurService entrepreneurService;

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(entrepreneurService.createProduct(productDto));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(entrepreneurService.updateProduct(id, productDto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        entrepreneurService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<?> getMyProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(entrepreneurService.getMyProducts(page, size));
        }
        return ResponseEntity.ok(entrepreneurService.getMyProducts());
    }

    @GetMapping("/requests/incoming")
    public ResponseEntity<?> getIncomingRequests(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(entrepreneurService.getIncomingRequests(page, size));
        }
        return ResponseEntity.ok(entrepreneurService.getIncomingRequests());
    }

    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<ServiceRequestDto> acceptRequest(@PathVariable Long id) {
        return ResponseEntity.ok(entrepreneurService.acceptRequest(id));
    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<ServiceRequestDto> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(entrepreneurService.rejectRequest(id));
    }

    @PutMapping("/requests/{id}/complete")
    public ResponseEntity<ServiceRequestDto> completeRequest(@PathVariable Long id) {
        return ResponseEntity.ok(entrepreneurService.completeRequest(id));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getMyOrders(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            return ResponseEntity.ok(entrepreneurService.getMyOrders(page, size));
        }
        return ResponseEntity.ok(entrepreneurService.getMyOrders());
    }

    @GetMapping("/earnings")
    public ResponseEntity<Map<String, Double>> getEarningsSummary() {
        Double earnings = entrepreneurService.getEarningsSummary();
        return ResponseEntity.ok(Map.of("earnings", earnings));
    }
}
