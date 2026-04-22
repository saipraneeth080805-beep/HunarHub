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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntrepreneurProfileRepository entrepreneurProfileRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NotificationService notificationService;

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream()
                .filter(cat -> !"Vendor".equalsIgnoreCase(cat.getName()))
                .map(cat -> new CategoryDto(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<EntrepreneurDto> getAllEntrepreneurs() {
        List<EntrepreneurProfile> profiles = entrepreneurProfileRepository
                .findByApprovalStatus(EntrepreneurProfile.ApprovalStatus.APPROVED);

        return profiles.stream().map(this::convertToEntrepreneurDto).collect(Collectors.toList());
    }

    public PageResponse<EntrepreneurDto> getAllEntrepreneurs(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<EntrepreneurProfile> profiles;
        
        if (search != null && !search.trim().isEmpty()) {
            profiles = entrepreneurProfileRepository.findByApprovalStatusAndSearch(
                EntrepreneurProfile.ApprovalStatus.APPROVED, search.trim(), pageable);
        } else {
            profiles = entrepreneurProfileRepository.findByApprovalStatus(
                EntrepreneurProfile.ApprovalStatus.APPROVED, pageable);
        }

        List<EntrepreneurDto> content = profiles.getContent().stream()
                .map(this::convertToEntrepreneurDto)
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

    public EntrepreneurDto getEntrepreneurById(Long id) {
        return convertToEntrepreneurDto(requireApprovedEntrepreneurProfile(id));
    }

    /**
     * Supports both entrepreneur profile id and user id for backward compatibility.
     */
    private EntrepreneurProfile requireApprovedEntrepreneurProfile(Long entrepreneurIdentifier) {
        EntrepreneurProfile profile = entrepreneurProfileRepository.findById(entrepreneurIdentifier)
                .orElseGet(() -> userRepository.findById(entrepreneurIdentifier)
                        .flatMap(entrepreneurProfileRepository::findByUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Entrepreneur not found")));
        if (profile.getApprovalStatus() != EntrepreneurProfile.ApprovalStatus.APPROVED) {
            throw new BadRequestException("Entrepreneur not approved");
        }
        return profile;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findByAvailableTrue();
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PageResponse<ProductDto> getAllProducts(int page, int size, Long categoryId, 
                                                   Double minPrice, Double maxPrice, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.searchProducts(
            categoryId, minPrice, maxPrice, 
            search != null ? search.trim() : "", pageable);

        List<ProductDto> content = products.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
            content,
            products.getNumber(),
            products.getSize(),
            products.getTotalElements(),
            products.getTotalPages(),
            products.isLast(),
            products.isFirst()
        );
    }

    public List<ProductDto> getProductsByEntrepreneur(Long entrepreneurId) {
        User entrepreneur = requireApprovedEntrepreneurProfile(entrepreneurId).getUser();

        List<Product> products = productRepository.findByEntrepreneur(entrepreneur);
        return products.stream()
                .filter(Product::getAvailable)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByEntrepreneur(Long entrepreneurId) {
        User entrepreneur = requireApprovedEntrepreneurProfile(entrepreneurId).getUser();

        return reviewRepository.findByEntrepreneur(entrepreneur).stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToDto(product);
    }

    @Transactional
    public ServiceRequestDto createServiceRequest(Long entrepreneurId, String serviceDescription) {
        User customer = getCurrentUser();
        EntrepreneurProfile profile = requireApprovedEntrepreneurProfile(entrepreneurId);
        User entrepreneur = profile.getUser();

        if (customer.getId().equals(entrepreneur.getId())) {
            throw new BadRequestException("Cannot create request for yourself");
        }

        ServiceRequest request = new ServiceRequest();
        request.setCustomer(customer);
        request.setEntrepreneur(entrepreneur);
        request.setServiceDescription(serviceDescription);
        request.setStatus(ServiceRequest.RequestStatus.PENDING);

        request = serviceRequestRepository.save(request);

        notificationService.createNotification(
                entrepreneur,
                "New service request",
                customer.getName() + " sent you a service request. Request #" + request.getId() + ".",
                "SERVICE_REQUEST_CREATED",
                null,
                null,
                null
        );

        return convertToDto(request);
    }

    @Transactional
    public OrderDto createOrder(Long productId, Integer quantity) {
        User customer = getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getAvailable()) {
            throw new BadRequestException("Product is not available");
        }

        Double totalPrice = product.getPrice() * quantity;

        Order order = new Order();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setStatus(Order.OrderStatus.PLACED);

        order = orderRepository.save(order);

        User seller = product.getEntrepreneur();
        if (!seller.getId().equals(customer.getId())) {
            notificationService.createNotification(
                    seller,
                    "New order received",
                    customer.getName() + " placed an order for \"" + product.getName() + "\" (Qty: " + quantity + "). Order #" + order.getId() + ".",
                    "ORDER_PLACED",
                    null,
                    order.getId(),
                    product.getId()
            );
        }

        return convertToOrderDto(order);
    }

    public List<OrderDto> getMyOrders() {
        User customer = getCurrentUser();
        List<Order> orders = orderRepository.findByCustomer(customer);
        return orders.stream().map(this::convertToOrderDto).collect(Collectors.toList());
    }

    public PageResponse<OrderDto> getMyOrders(int page, int size) {
        User customer = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<Order> orders = orderRepository.findByCustomer(customer, pageable);

        List<OrderDto> content = orders.getContent().stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
            content,
            orders.getNumber(),
            orders.getSize(),
            orders.getTotalElements(),
            orders.getTotalPages(),
            orders.isLast(),
            orders.isFirst()
        );
    }

    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        User customer = getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("You can only cancel your own orders");
        }
        if (order.getStatus() != Order.OrderStatus.PLACED && order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new BadRequestException("Only placed or confirmed orders can be cancelled");
        }
        order.setStatus(Order.OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return convertToOrderDto(order);
    }

    public List<ServiceRequestDto> getMyServiceRequests() {
        User customer = getCurrentUser();
        List<ServiceRequest> requests = serviceRequestRepository.findByCustomer(customer);
        return requests.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PageResponse<ServiceRequestDto> getMyServiceRequests(int page, int size) {
        User customer = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("requestedDate").descending());
        Page<ServiceRequest> requests = serviceRequestRepository.findByCustomer(customer, pageable);

        List<ServiceRequestDto> content = requests.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
            content,
            requests.getNumber(),
            requests.getSize(),
            requests.getTotalElements(),
            requests.getTotalPages(),
            requests.isLast(),
            requests.isFirst()
        );
    }

    @Transactional
    public ReviewDto createReview(Long entrepreneurId, Integer rating, String comment) {
        User customer = getCurrentUser();
        User entrepreneur = requireApprovedEntrepreneurProfile(entrepreneurId).getUser();
        Long entrepreneurUserId = entrepreneur.getId();

        // Customer can review after completing either an order or a service request.
        List<Order> completedOrders = orderRepository.findByCustomer(customer).stream()
                .filter(order -> order.getProduct().getEntrepreneur().getId().equals(entrepreneurUserId))
                .filter(order -> order.getStatus() == Order.OrderStatus.DELIVERED)
                .collect(Collectors.toList());

        List<ServiceRequest> completedRequests = serviceRequestRepository.findByCustomer(customer).stream()
                .filter(request -> request.getEntrepreneur().getId().equals(entrepreneurUserId))
                .filter(request -> request.getStatus() == ServiceRequest.RequestStatus.COMPLETED)
                .collect(Collectors.toList());

        if (completedOrders.isEmpty() && completedRequests.isEmpty()) {
            throw new BadRequestException("You can only review after a completed order or service");
        }

        if (reviewRepository.existsByCustomerAndEntrepreneur(customer, entrepreneur)) {
            throw new BadRequestException("You have already reviewed this entrepreneur");
        }

        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        Review review = new Review();
        review.setCustomer(customer);
        review.setEntrepreneur(entrepreneur);
        review.setRating(rating);
        review.setComment(comment);

        review = reviewRepository.save(review);
        return convertToReviewDto(review);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setAdditionalImageUrls(product.getAdditionalImageUrls());
        dto.setEntrepreneurId(product.getEntrepreneur().getId());
        dto.setEntrepreneurName(product.getEntrepreneur().getName());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        dto.setAvailable(product.getAvailable());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setAvailableSizes(product.getAvailableSizes());
        dto.setFabric(product.getFabric());
        dto.setColor(product.getColor());
        dto.setFit(product.getFit());
        dto.setWashCare(product.getWashCare());
        dto.setStyleNotes(product.getStyleNotes());
        dto.setSpec1(product.getSpec1());
        dto.setSpec2(product.getSpec2());
        dto.setQuantityAvailable(product.getQuantityAvailable());
        dto.setShortDescription(product.getShortDescription());
        dto.setPotterProductType(product.getPotterProductType());
        dto.setDetailedDescription(product.getDetailedDescription());
        dto.setHeight(product.getHeight());
        dto.setDiameterTop(product.getDiameterTop());
        dto.setDiameterBottom(product.getDiameterBottom());
        dto.setCapacity(product.getCapacity());
        dto.setMaterial(product.getMaterial());
        dto.setFinishType(product.getFinishType());
        dto.setHandmadeOrMachine(product.getHandmadeOrMachine());
        dto.setSuitableFor(product.getSuitableFor());
        dto.setUsageEnvironment(product.getUsageEnvironment());
        dto.setFoodSafe(product.getFoodSafe());
        dto.setDrainageHole(product.getDrainageHole());
        dto.setWeightApprox(product.getWeightApprox());
        dto.setFragile(product.getFragile());
        dto.setHandlingInstructions(product.getHandlingInstructions());
        dto.setDeliveryTime(product.getDeliveryTime());
        dto.setPackagingDetails(product.getPackagingDetails());
        dto.setHandmadeByArtisan(product.getHandmadeByArtisan());
        dto.setOriginStory(product.getOriginStory());
        dto.setEcoFriendly(product.getEcoFriendly());
        dto.setReturnPolicy(product.getReturnPolicy());
        dto.setReplacementPolicy(product.getReplacementPolicy());
        return dto;
    }

    private ServiceRequestDto convertToDto(ServiceRequest request) {
        ServiceRequestDto dto = new ServiceRequestDto();
        dto.setId(request.getId());
        dto.setCustomerId(request.getCustomer().getId());
        dto.setCustomerName(request.getCustomer().getName());
        dto.setEntrepreneurId(request.getEntrepreneur().getId());
        dto.setEntrepreneurName(request.getEntrepreneur().getName());
        dto.setServiceDescription(request.getServiceDescription());
        dto.setStatus(request.getStatus());
        dto.setRequestedDate(request.getRequestedDate());
        dto.setScheduledDate(request.getScheduledDate());
        return dto;
    }

    private OrderDto convertToOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setCustomerEmail(order.getCustomer().getEmail());
        Product product = order.getProduct();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductDescription(product.getDescription());
        dto.setProductPrice(product.getPrice());
        dto.setProductImageUrl(product.getImageUrl());
        dto.setCategoryName(product.getCategory().getName());
        dto.setEntrepreneurId(product.getEntrepreneur().getId());
        dto.setEntrepreneurName(product.getEntrepreneur().getName());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }

    private ReviewDto convertToReviewDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCustomerId(review.getCustomer().getId());
        dto.setCustomerName(review.getCustomer().getName());
        dto.setEntrepreneurId(review.getEntrepreneur().getId());
        dto.setEntrepreneurName(review.getEntrepreneur().getName());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    @Transactional
    public DisputeDto createDispute(CreateDisputeRequest request) {
        User reporter = getCurrentUser();
        
        Dispute dispute = new Dispute();
        dispute.setReporter(reporter);
        dispute.setDisputeType(Dispute.DisputeType.valueOf(request.getDisputeType().toUpperCase()));
        dispute.setTitle(request.getTitle());
        dispute.setDescription(request.getDescription());
        dispute.setStatus(Dispute.DisputeStatus.OPEN);

        User reportedUser = null;
        String identifier = request.getReportedUserIdentifier() != null ? request.getReportedUserIdentifier().trim() : "";
        if (!identifier.isEmpty()) {
            if (identifier.contains("@")) {
                reportedUser = userRepository.findByEmailIgnoreCase(identifier)
                        .orElseThrow(() -> new ResourceNotFoundException("Reported user not found"));
            } else {
                reportedUser = userRepository.findFirstByNameIgnoreCase(identifier)
                        .orElseThrow(() -> new ResourceNotFoundException("Reported user not found"));
            }
        } else if (request.getReportedUserId() != null) {
            reportedUser = userRepository.findById(request.getReportedUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reported user not found"));
        }

        if (reportedUser == null) {
            throw new BadRequestException("Reported user's email/username is required");
        }
        dispute.setReportedUser(reportedUser);
        
        if (request.getOrderId() != null) {
            Order order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            dispute.setOrder(order);
        }
        
        if (request.getServiceRequestId() != null) {
            ServiceRequest serviceRequest = serviceRequestRepository.findById(request.getServiceRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));
            dispute.setServiceRequest(serviceRequest);
        }
        
        dispute = disputeRepository.save(dispute);

        // Notify the reported user (in-app)
        if (dispute.getReportedUser() != null && !dispute.getReportedUser().getId().equals(reporter.getId())) {
            notificationService.createNotification(
                    dispute.getReportedUser(),
                    "New complaint raised",
                    reporter.getName() + " raised a complaint against you: " + dispute.getTitle(),
                    "COMPLAINT_RAISED",
                    dispute.getId(),
                    null,
                    null
            );
        }

        return convertDisputeToDto(dispute);
    }
    
    public List<DisputeDto> getMyDisputes() {
        User reporter = getCurrentUser();
        return disputeRepository.findByReporterId(reporter.getId()).stream()
                .map(this::convertDisputeToDto)
                .collect(Collectors.toList());
    }

    public List<DisputeDto> getDisputesAgainstMe() {
        User me = getCurrentUser();
        return disputeRepository.findByReportedUserId(me.getId()).stream()
                .map(this::convertDisputeToDto)
                .collect(Collectors.toList());
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

    private EntrepreneurDto convertToEntrepreneurDto(EntrepreneurProfile profile) {
        EntrepreneurDto dto = new EntrepreneurDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setName(profile.getUser().getName());
        dto.setEmail(profile.getUser().getEmail());
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
