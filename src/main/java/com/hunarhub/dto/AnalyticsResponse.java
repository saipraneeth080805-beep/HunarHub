package com.hunarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private Long totalUsers;
    private Long totalCustomers;
    private Long totalEntrepreneurs;
    private Long pendingEntrepreneurs;
    private Long approvedEntrepreneurs;
    private Long totalOrders;
    private Long totalServiceRequests;
    private Long totalProducts;
    private Long totalDisputes;
    private Long openDisputes;
    private Double totalRevenue;
    private Map<String, Long> ordersByStatus;
    private Map<String, Long> requestsByStatus;
    private Map<String, Long> disputesByStatus;
    private Map<String, Long> usersByRole;
    private Map<String, Long> ordersByCategory;
}
