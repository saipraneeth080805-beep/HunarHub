package com.hunarhub.service;

import com.hunarhub.dto.*;
import com.hunarhub.entity.*;
import com.hunarhub.exception.BadRequestException;
import com.hunarhub.exception.ResourceNotFoundException;
import com.hunarhub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private EntrepreneurProfileRepository entrepreneurProfileRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private DisputeRepository disputeRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private NotificationService notificationService;

    public List<EntrepreneurDto> getPendingEntrepreneurs() {
        List<EntrepreneurProfile> profiles = entrepreneurProfileRepository
                .findByApprovalStatus(EntrepreneurProfile.ApprovalStatus.PENDING);

        return profiles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<EntrepreneurDto> getAllEntrepreneurs() {
        List<EntrepreneurProfile> profiles = entrepreneurProfileRepository.findAll();
        return profiles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PageResponse<EntrepreneurDto> getPendingEntrepreneurs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<EntrepreneurProfile> profiles = entrepreneurProfileRepository.findByApprovalStatus(
            EntrepreneurProfile.ApprovalStatus.PENDING, pageable);

        List<EntrepreneurDto> content = profiles.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
            content,
            profiles.getNumber(),
            profiles.getSize(),
            profiles.getTotalElements(),
            profiles.getTotalPages(),
            profiles.isLast(),
            profiles.isFirst()
        );
    }

    @Transactional
    public EntrepreneurDto approveEntrepreneur(Long entrepreneurId) {
        EntrepreneurProfile profile = entrepreneurProfileRepository.findById(entrepreneurId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepreneur profile not found"));

        EntrepreneurProfile.ApprovalStatus previousStatus = profile.getApprovalStatus();
        profile.setApprovalStatus(EntrepreneurProfile.ApprovalStatus.APPROVED);
        profile = entrepreneurProfileRepository.save(profile);

        if (previousStatus != EntrepreneurProfile.ApprovalStatus.APPROVED) {
            notificationService.createNotification(
                    profile.getUser(),
                    "Entrepreneur request approved",
                    "Your entrepreneur request has been accepted by admin. You can now access entrepreneur features.",
                    "ENTREPRENEUR_APPROVED",
                    null,
                    null,
                    null
            );
        }

        return convertToDto(profile);
    }

    @Transactional
    public EntrepreneurDto rejectEntrepreneur(Long entrepreneurId) {
        EntrepreneurProfile profile = entrepreneurProfileRepository.findById(entrepreneurId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepreneur profile not found"));

        EntrepreneurProfile.ApprovalStatus previousStatus = profile.getApprovalStatus();
        profile.setApprovalStatus(EntrepreneurProfile.ApprovalStatus.REJECTED);
        profile = entrepreneurProfileRepository.save(profile);

        if (previousStatus != EntrepreneurProfile.ApprovalStatus.REJECTED) {
            notificationService.createNotification(
                    profile.getUser(),
                    "Entrepreneur request update",
                    "Your entrepreneur registration was not approved. Please contact support if you have questions.",
                    "ENTREPRENEUR_REJECTED",
                    null,
                    null,
                    null
            );
        }

        return convertToDto(profile);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    public PageResponse<OrderDto> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<Order> orders = orderRepository.findAll(pageable);

        return new PageResponse<>(
            orders.getContent().stream().map(this::convertOrderToDto).collect(Collectors.toList()),
            orders.getNumber(),
            orders.getSize(),
            orders.getTotalElements(),
            orders.getTotalPages(),
            orders.isLast(),
            orders.isFirst()
        );
    }

    public List<ServiceRequest> getAllServiceRequests() {
        return serviceRequestRepository.findAll();
    }

    public PageResponse<ServiceRequest> getAllServiceRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("requestedDate").descending());
        Page<ServiceRequest> requests = serviceRequestRepository.findAll(pageable);

        return new PageResponse<>(
            requests.getContent(),
            requests.getNumber(),
            requests.getSize(),
            requests.getTotalElements(),
            requests.getTotalPages(),
            requests.isLast(),
            requests.isFirst()
        );
    }

    // Category Management
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryDto(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category already exists");
        }
        Category category = new Category();
        category.setName(request.getName());
        category = categoryRepository.save(category);
        return new CategoryDto(category.getId(), category.getName());
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        if (!category.getName().equals(request.getName()) && 
            categoryRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Category name already exists");
        }
        
        category.setName(request.getName());
        category = categoryRepository.save(category);
        return new CategoryDto(category.getId(), category.getName());
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        // Check if category is used by any products
        if (productRepository.countByCategoryId(id) > 0) {
            throw new BadRequestException("Cannot delete category that has products");
        }
        
        categoryRepository.delete(category);
    }

    // Dispute Management
    public List<DisputeDto> getAllDisputes() {
        return disputeRepository.findAll().stream()
                .map(this::convertDisputeToDto)
                .collect(Collectors.toList());
    }

    public List<DisputeDto> getDisputesByStatus(String status) {
        Dispute.DisputeStatus disputeStatus = Dispute.DisputeStatus.valueOf(status.toUpperCase());
        return disputeRepository.findByStatus(disputeStatus).stream()
                .map(this::convertDisputeToDto)
                .collect(Collectors.toList());
    }

    public DisputeDto getDisputeById(Long id) {
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found"));
        return convertDisputeToDto(dispute);
    }

    @Transactional
    public DisputeDto updateDisputeStatus(Long id, String status, String adminResponse) {
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found"));
        
        Dispute.DisputeStatus newStatus = Dispute.DisputeStatus.valueOf(status.toUpperCase());
        dispute.setStatus(newStatus);
        dispute.setAdminResponse(adminResponse);
        
        if (newStatus == Dispute.DisputeStatus.RESOLVED || newStatus == Dispute.DisputeStatus.CLOSED) {
            dispute.setResolvedAt(LocalDateTime.now());
        }
        
        dispute = disputeRepository.save(dispute);

        if (newStatus == Dispute.DisputeStatus.RESOLVED || newStatus == Dispute.DisputeStatus.CLOSED) {
            String resolutionNote = adminResponse != null && !adminResponse.isBlank()
                    ? " Admin note: " + adminResponse
                    : "";
            User reporter = dispute.getReporter();
            User reported = dispute.getReportedUser();
            String title = newStatus == Dispute.DisputeStatus.RESOLVED ? "Complaint resolved" : "Complaint closed";
            String baseMsg = "Regarding \"" + dispute.getTitle() + "\"." + resolutionNote;
            notificationService.createNotification(
                    reporter,
                    title,
                    "Your complaint has been updated. " + baseMsg,
                    "COMPLAINT_RESOLVED",
                    dispute.getId(),
                    null,
                    null
            );
            if (reported != null && !reported.getId().equals(reporter.getId())) {
                notificationService.createNotification(
                        reported,
                        title,
                        "A complaint you were involved in has been updated. " + baseMsg,
                        "COMPLAINT_RESOLVED",
                        dispute.getId(),
                        null,
                        null
                );
            }
        }

        return convertDisputeToDto(dispute);
    }

    // Analytics and Reports
    public AnalyticsResponse getAnalytics() {
        AnalyticsResponse analytics = new AnalyticsResponse();
        
        // User statistics
        long totalUsers = userRepository.count();
        long totalCustomers = userRepository.countByRole(User.Role.CUSTOMER);
        long totalEntrepreneurs = userRepository.countByRole(User.Role.ENTREPRENEUR);
        long pendingEntrepreneurs = entrepreneurProfileRepository.countByApprovalStatus(EntrepreneurProfile.ApprovalStatus.PENDING);
        long approvedEntrepreneurs = entrepreneurProfileRepository.countByApprovalStatus(EntrepreneurProfile.ApprovalStatus.APPROVED);
        
        analytics.setTotalUsers(totalUsers);
        analytics.setTotalCustomers(totalCustomers);
        analytics.setTotalEntrepreneurs(totalEntrepreneurs);
        analytics.setPendingEntrepreneurs(pendingEntrepreneurs);
        analytics.setApprovedEntrepreneurs(approvedEntrepreneurs);
        
        // Order statistics
        long totalOrders = orderRepository.count();
        analytics.setTotalOrders(totalOrders);
        
        // Calculate total revenue (from delivered orders)
        Double totalRevenue = orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.DELIVERED)
                .mapToDouble(Order::getTotalPrice)
                .sum();
        analytics.setTotalRevenue(totalRevenue);
        
        // Orders by status
        Map<String, Long> ordersByStatus = new HashMap<>();
        for (Order.OrderStatus status : Order.OrderStatus.values()) {
            ordersByStatus.put(status.name(), orderRepository.countByStatus(status));
        }
        analytics.setOrdersByStatus(ordersByStatus);
        
        // Service request statistics
        long totalServiceRequests = serviceRequestRepository.count();
        analytics.setTotalServiceRequests(totalServiceRequests);
        
        // Requests by status
        Map<String, Long> requestsByStatus = new HashMap<>();
        for (ServiceRequest.RequestStatus status : ServiceRequest.RequestStatus.values()) {
            requestsByStatus.put(status.name(), serviceRequestRepository.countByStatus(status));
        }
        analytics.setRequestsByStatus(requestsByStatus);
        
        // Product statistics
        long totalProducts = productRepository.count();
        analytics.setTotalProducts(totalProducts);
        
        // Dispute statistics
        long totalDisputes = disputeRepository.count();
        long openDisputes = disputeRepository.countByStatus(Dispute.DisputeStatus.OPEN);
        analytics.setTotalDisputes(totalDisputes);
        analytics.setOpenDisputes(openDisputes);
        
        // Disputes by status
        Map<String, Long> disputesByStatus = new HashMap<>();
        for (Dispute.DisputeStatus status : Dispute.DisputeStatus.values()) {
            disputesByStatus.put(status.name(), disputeRepository.countByStatus(status));
        }
        analytics.setDisputesByStatus(disputesByStatus);
        
        // Users by role
        Map<String, Long> usersByRole = new HashMap<>();
        for (User.Role role : User.Role.values()) {
            usersByRole.put(role.name(), userRepository.countByRole(role));
        }
        analytics.setUsersByRole(usersByRole);
        
        // Orders by category (simplified - would need join query for better accuracy)
        Map<String, Long> ordersByCategory = new HashMap<>();
        // This is a simplified version - in production, you'd want a proper join query
        analytics.setOrdersByCategory(ordersByCategory);
        
        return analytics;
    }

    // User listings for admin
    public List<UserSummaryDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertUserToSummary)
                .collect(Collectors.toList());
    }

    private UserSummaryDto convertUserToSummary(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    private DisputeDto convertDisputeToDto(Dispute dispute) {
        DisputeDto dto = new DisputeDto();
        dto.setId(dispute.getId());
        dto.setReporterId(dispute.getReporter().getId());
        dto.setReporterName(dispute.getReporter().getName());
        dto.setReporterEmail(dispute.getReporter().getEmail());
        
        if (dispute.getReportedUser() != null) {
            dto.setReportedUserId(dispute.getReportedUser().getId());
            dto.setReportedUserName(dispute.getReportedUser().getName());
        }
        
        if (dispute.getOrder() != null) {
            dto.setOrderId(dispute.getOrder().getId());
        }
        
        if (dispute.getServiceRequest() != null) {
            dto.setServiceRequestId(dispute.getServiceRequest().getId());
        }
        
        dto.setDisputeType(dispute.getDisputeType().name());
        dto.setTitle(dispute.getTitle());
        dto.setDescription(dispute.getDescription());
        dto.setStatus(dispute.getStatus().name());
        dto.setAdminResponse(dispute.getAdminResponse());
        dto.setCreatedAt(dispute.getCreatedAt());
        dto.setResolvedAt(dispute.getResolvedAt());
        
        return dto;
    }

    private OrderDto convertOrderToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setCustomerEmail(order.getCustomer().getEmail());
        dto.setProductId(order.getProduct().getId());
        dto.setProductName(order.getProduct().getName());
        dto.setProductDescription(order.getProduct().getDescription());
        dto.setProductPrice(order.getProduct().getPrice());
        dto.setProductImageUrl(order.getProduct().getImageUrl());
        dto.setCategoryName(order.getProduct().getCategory().getName());
        dto.setEntrepreneurId(order.getProduct().getEntrepreneur().getId());
        dto.setEntrepreneurName(order.getProduct().getEntrepreneur().getName());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }

    private EntrepreneurDto convertToDto(EntrepreneurProfile profile) {
        User user = profile.getUser();
        EntrepreneurDto dto = new EntrepreneurDto();
        dto.setId(profile.getId());
        dto.setUserId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setSkills(profile.getSkills());
        dto.setExperience(profile.getExperience());
        dto.setDescription(profile.getDescription());
        dto.setBusinessCategory(profile.getBusinessCategory());
        dto.setShopName(profile.getShopName());
        dto.setOwnerName(profile.getOwnerName());
        dto.setShopAddress(profile.getShopAddress());
        dto.setShopPhone(profile.getShopPhone());
        dto.setShopEmail(profile.getShopEmail());
        dto.setShopExperience(profile.getShopExperience());
        dto.setShopDescription(profile.getShopDescription());
        dto.setApprovalStatus(profile.getApprovalStatus());
        dto.setEarnings(profile.getEarnings());
        return dto;
    }
}
