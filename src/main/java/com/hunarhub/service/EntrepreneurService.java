package com.hunarhub.service;

import com.hunarhub.dto.*;
import com.hunarhub.entity.*;
import com.hunarhub.exception.BadRequestException;
import com.hunarhub.exception.ResourceNotFoundException;
import com.hunarhub.exception.UnauthorizedException;
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
public class EntrepreneurService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntrepreneurProfileRepository entrepreneurProfileRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotificationService notificationService;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void checkEntrepreneurApproved(User user) {
        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("User is not an entrepreneur"));

        if (profile.getApprovalStatus() != EntrepreneurProfile.ApprovalStatus.APPROVED) {
            throw new BadRequestException("Your entrepreneur profile is not approved yet");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void validateArtisanProductFields(EntrepreneurProfile profile, ProductDto productDto) {
        if (profile == null || !"artisan".equalsIgnoreCase(profile.getBusinessCategory())) return;

        if (isBlank(productDto.getName())
                || productDto.getPrice() == null
                || isBlank(productDto.getDescription())
                || isBlank(productDto.getArtisanBrandName())
                || isBlank(productDto.getArtisanProductType())
                || isBlank(productDto.getArtisanMakingProcess())
                || isBlank(productDto.getArtisanUniqueFeatures())
                || isBlank(productDto.getArtisanDimensions())
                || isBlank(productDto.getArtisanWeight())
                || isBlank(productDto.getArtisanMaterial())
                || isBlank(productDto.getArtisanColorVariants())
                || isBlank(productDto.getArtisanQuantityType())
                || isBlank(productDto.getArtisanShippingCost())
                || productDto.getArtisanStockQuantity() == null
                || isBlank(productDto.getArtisanStockMode())
                || isBlank(productDto.getDeliveryTime())
                || isBlank(productDto.getArtisanShippingLocations())
                || isBlank(productDto.getArtisanReturnWindow())
                || isBlank(productDto.getArtisanReturnConditions())
                || isBlank(productDto.getArtisanRefundReplacementDetails())) {
            throw new BadRequestException("For Artisan entrepreneurs, all artisan product details are required.");
        }
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        User entrepreneur = getCurrentUser();
        checkEntrepreneurApproved(entrepreneur);
        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUser(entrepreneur)
                .orElseThrow(() -> new UnauthorizedException("User is not an entrepreneur"));
        validateArtisanProductFields(profile, productDto);

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setAdditionalImageUrls(productDto.getAdditionalImageUrls());
        product.setEntrepreneur(entrepreneur);
        product.setCategory(category);
        product.setAvailable(productDto.getAvailable() != null ? productDto.getAvailable() : true);
        product.setAvailableSizes(productDto.getAvailableSizes());
        product.setFabric(productDto.getFabric());
        product.setColor(productDto.getColor());
        product.setFit(productDto.getFit());
        product.setWashCare(productDto.getWashCare());
        product.setStyleNotes(productDto.getStyleNotes());
        product.setSpec1(productDto.getSpec1());
        product.setSpec2(productDto.getSpec2());
        product.setQuantityAvailable(productDto.getQuantityAvailable());
        product.setShortDescription(productDto.getShortDescription());
        product.setPotterProductType(productDto.getPotterProductType());
        product.setDetailedDescription(productDto.getDetailedDescription());
        product.setHeight(productDto.getHeight());
        product.setDiameterTop(productDto.getDiameterTop());
        product.setDiameterBottom(productDto.getDiameterBottom());
        product.setCapacity(productDto.getCapacity());
        product.setMaterial(productDto.getMaterial());
        product.setFinishType(productDto.getFinishType());
        product.setHandmadeOrMachine(productDto.getHandmadeOrMachine());
        product.setSuitableFor(productDto.getSuitableFor());
        product.setUsageEnvironment(productDto.getUsageEnvironment());
        product.setFoodSafe(productDto.getFoodSafe());
        product.setDrainageHole(productDto.getDrainageHole());
        product.setWeightApprox(productDto.getWeightApprox());
        product.setFragile(productDto.getFragile());
        product.setHandlingInstructions(productDto.getHandlingInstructions());
        product.setDeliveryTime(productDto.getDeliveryTime());
        product.setPackagingDetails(productDto.getPackagingDetails());
        product.setHandmadeByArtisan(productDto.getHandmadeByArtisan());
        product.setOriginStory(productDto.getOriginStory());
        product.setEcoFriendly(productDto.getEcoFriendly());
        product.setReturnPolicy(productDto.getReturnPolicy());
        product.setReplacementPolicy(productDto.getReplacementPolicy());
        product.setArtisanBrandName(productDto.getArtisanBrandName());
        product.setArtisanProductType(productDto.getArtisanProductType());
        product.setArtisanMakingProcess(productDto.getArtisanMakingProcess());
        product.setArtisanUniqueFeatures(productDto.getArtisanUniqueFeatures());
        product.setArtisanDimensions(productDto.getArtisanDimensions());
        product.setArtisanWeight(productDto.getArtisanWeight());
        product.setArtisanMaterial(productDto.getArtisanMaterial());
        product.setArtisanColorVariants(productDto.getArtisanColorVariants());
        product.setArtisanQuantityType(productDto.getArtisanQuantityType());
        product.setArtisanDiscount(productDto.getArtisanDiscount());
        product.setArtisanTaxesIncluded(productDto.getArtisanTaxesIncluded());
        product.setArtisanShippingCost(productDto.getArtisanShippingCost());
        product.setArtisanStockQuantity(productDto.getArtisanStockQuantity());
        product.setArtisanStockMode(productDto.getArtisanStockMode());
        product.setArtisanRestockTimeline(productDto.getArtisanRestockTimeline());
        product.setArtisanShippingLocations(productDto.getArtisanShippingLocations());
        product.setArtisanCourierPartner(productDto.getArtisanCourierPartner());
        product.setArtisanReturnWindow(productDto.getArtisanReturnWindow());
        product.setArtisanReturnConditions(productDto.getArtisanReturnConditions());
        product.setArtisanRefundReplacementDetails(productDto.getArtisanRefundReplacementDetails());

        product = productRepository.save(product);

        for (User admin : userRepository.findByRole(User.Role.ADMIN)) {
            notificationService.createNotification(
                    admin,
                    "New product listed",
                    entrepreneur.getName() + " added a new product: \"" + product.getName() + "\".",
                    "NEW_PRODUCT",
                    null,
                    null,
                    product.getId()
            );
        }

        return convertToDto(product);
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        User entrepreneur = getCurrentUser();
        checkEntrepreneurApproved(entrepreneur);
        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUser(entrepreneur)
                .orElseThrow(() -> new UnauthorizedException("User is not an entrepreneur"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getEntrepreneur().getId().equals(entrepreneur.getId())) {
            throw new UnauthorizedException("You can only update your own products");
        }

        if (productDto.getName() != null) product.setName(productDto.getName());
        if (productDto.getDescription() != null) product.setDescription(productDto.getDescription());
        if (productDto.getPrice() != null) product.setPrice(productDto.getPrice());
        if (productDto.getImageUrl() != null) product.setImageUrl(productDto.getImageUrl());
        if (productDto.getAdditionalImageUrls() != null) product.setAdditionalImageUrls(productDto.getAdditionalImageUrls());
        if (productDto.getAvailable() != null) product.setAvailable(productDto.getAvailable());
        if (productDto.getAvailableSizes() != null) product.setAvailableSizes(productDto.getAvailableSizes());
        if (productDto.getFabric() != null) product.setFabric(productDto.getFabric());
        if (productDto.getColor() != null) product.setColor(productDto.getColor());
        if (productDto.getFit() != null) product.setFit(productDto.getFit());
        if (productDto.getWashCare() != null) product.setWashCare(productDto.getWashCare());
        if (productDto.getStyleNotes() != null) product.setStyleNotes(productDto.getStyleNotes());
        if (productDto.getSpec1() != null) product.setSpec1(productDto.getSpec1());
        if (productDto.getSpec2() != null) product.setSpec2(productDto.getSpec2());
        if (productDto.getQuantityAvailable() != null) product.setQuantityAvailable(productDto.getQuantityAvailable());
        if (productDto.getShortDescription() != null) product.setShortDescription(productDto.getShortDescription());
        if (productDto.getPotterProductType() != null) product.setPotterProductType(productDto.getPotterProductType());
        if (productDto.getDetailedDescription() != null) product.setDetailedDescription(productDto.getDetailedDescription());
        if (productDto.getHeight() != null) product.setHeight(productDto.getHeight());
        if (productDto.getDiameterTop() != null) product.setDiameterTop(productDto.getDiameterTop());
        if (productDto.getDiameterBottom() != null) product.setDiameterBottom(productDto.getDiameterBottom());
        if (productDto.getCapacity() != null) product.setCapacity(productDto.getCapacity());
        if (productDto.getMaterial() != null) product.setMaterial(productDto.getMaterial());
        if (productDto.getFinishType() != null) product.setFinishType(productDto.getFinishType());
        if (productDto.getHandmadeOrMachine() != null) product.setHandmadeOrMachine(productDto.getHandmadeOrMachine());
        if (productDto.getSuitableFor() != null) product.setSuitableFor(productDto.getSuitableFor());
        if (productDto.getUsageEnvironment() != null) product.setUsageEnvironment(productDto.getUsageEnvironment());
        if (productDto.getFoodSafe() != null) product.setFoodSafe(productDto.getFoodSafe());
        if (productDto.getDrainageHole() != null) product.setDrainageHole(productDto.getDrainageHole());
        if (productDto.getWeightApprox() != null) product.setWeightApprox(productDto.getWeightApprox());
        if (productDto.getFragile() != null) product.setFragile(productDto.getFragile());
        if (productDto.getHandlingInstructions() != null) product.setHandlingInstructions(productDto.getHandlingInstructions());
        if (productDto.getDeliveryTime() != null) product.setDeliveryTime(productDto.getDeliveryTime());
        if (productDto.getPackagingDetails() != null) product.setPackagingDetails(productDto.getPackagingDetails());
        if (productDto.getHandmadeByArtisan() != null) product.setHandmadeByArtisan(productDto.getHandmadeByArtisan());
        if (productDto.getOriginStory() != null) product.setOriginStory(productDto.getOriginStory());
        if (productDto.getEcoFriendly() != null) product.setEcoFriendly(productDto.getEcoFriendly());
        if (productDto.getReturnPolicy() != null) product.setReturnPolicy(productDto.getReturnPolicy());
        if (productDto.getReplacementPolicy() != null) product.setReplacementPolicy(productDto.getReplacementPolicy());
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        if (productDto.getArtisanBrandName() != null) product.setArtisanBrandName(productDto.getArtisanBrandName());
        if (productDto.getArtisanProductType() != null) product.setArtisanProductType(productDto.getArtisanProductType());
        if (productDto.getArtisanMakingProcess() != null) product.setArtisanMakingProcess(productDto.getArtisanMakingProcess());
        if (productDto.getArtisanUniqueFeatures() != null) product.setArtisanUniqueFeatures(productDto.getArtisanUniqueFeatures());
        if (productDto.getArtisanDimensions() != null) product.setArtisanDimensions(productDto.getArtisanDimensions());
        if (productDto.getArtisanWeight() != null) product.setArtisanWeight(productDto.getArtisanWeight());
        if (productDto.getArtisanMaterial() != null) product.setArtisanMaterial(productDto.getArtisanMaterial());
        if (productDto.getArtisanColorVariants() != null) product.setArtisanColorVariants(productDto.getArtisanColorVariants());
        if (productDto.getArtisanQuantityType() != null) product.setArtisanQuantityType(productDto.getArtisanQuantityType());
        if (productDto.getArtisanDiscount() != null) product.setArtisanDiscount(productDto.getArtisanDiscount());
        if (productDto.getArtisanTaxesIncluded() != null) product.setArtisanTaxesIncluded(productDto.getArtisanTaxesIncluded());
        if (productDto.getArtisanShippingCost() != null) product.setArtisanShippingCost(productDto.getArtisanShippingCost());
        if (productDto.getArtisanStockQuantity() != null) product.setArtisanStockQuantity(productDto.getArtisanStockQuantity());
        if (productDto.getArtisanStockMode() != null) product.setArtisanStockMode(productDto.getArtisanStockMode());
        if (productDto.getArtisanRestockTimeline() != null) product.setArtisanRestockTimeline(productDto.getArtisanRestockTimeline());
        if (productDto.getArtisanShippingLocations() != null) product.setArtisanShippingLocations(productDto.getArtisanShippingLocations());
        if (productDto.getArtisanCourierPartner() != null) product.setArtisanCourierPartner(productDto.getArtisanCourierPartner());
        if (productDto.getArtisanReturnWindow() != null) product.setArtisanReturnWindow(productDto.getArtisanReturnWindow());
        if (productDto.getArtisanReturnConditions() != null) product.setArtisanReturnConditions(productDto.getArtisanReturnConditions());
        if (productDto.getArtisanRefundReplacementDetails() != null) product.setArtisanRefundReplacementDetails(productDto.getArtisanRefundReplacementDetails());

        ProductDto mergedDto = convertToDto(product);
        validateArtisanProductFields(profile, mergedDto);

        product = productRepository.save(product);
        return convertToDto(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        User entrepreneur = getCurrentUser();
        checkEntrepreneurApproved(entrepreneur);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getEntrepreneur().getId().equals(entrepreneur.getId())) {
            throw new UnauthorizedException("You can only delete your own products");
        }

        // Delete all orders linked to this product before deleting the product itself
        orderRepository.deleteByProduct(product);
        productRepository.delete(product);
    }

    public List<ProductDto> getMyProducts() {
        User entrepreneur = getCurrentUser();
        List<Product> products = productRepository.findByEntrepreneur(entrepreneur);
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PageResponse<ProductDto> getMyProducts(int page, int size) {
        User entrepreneur = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findByEntrepreneur(entrepreneur, pageable);

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

    public List<ServiceRequestDto> getIncomingRequests() {
        User entrepreneur = getCurrentUser();
        List<ServiceRequest> requests = serviceRequestRepository.findByEntrepreneur(entrepreneur);
        return requests.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PageResponse<ServiceRequestDto> getIncomingRequests(int page, int size) {
        User entrepreneur = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("requestedDate").descending());
        Page<ServiceRequest> requests = serviceRequestRepository.findByEntrepreneur(entrepreneur, pageable);

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
    public ServiceRequestDto acceptRequest(Long requestId) {
        User entrepreneur = getCurrentUser();
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));

        if (!request.getEntrepreneur().getId().equals(entrepreneur.getId())) {
            throw new UnauthorizedException("You can only accept requests sent to you");
        }

        if (request.getStatus() != ServiceRequest.RequestStatus.PENDING) {
            throw new BadRequestException("Request is not in PENDING status");
        }

        request.setStatus(ServiceRequest.RequestStatus.ACCEPTED);
        request = serviceRequestRepository.save(request);

        notificationService.createNotification(
                request.getCustomer(),
                "Service request accepted",
                entrepreneur.getName() + " accepted your service request. Request #" + request.getId() + ".",
                "SERVICE_REQUEST_ACCEPTED",
                null,
                null,
                null
        );

        return convertToDto(request);
    }

    @Transactional
    public ServiceRequestDto rejectRequest(Long requestId) {
        User entrepreneur = getCurrentUser();
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));

        if (!request.getEntrepreneur().getId().equals(entrepreneur.getId())) {
            throw new UnauthorizedException("You can only reject requests sent to you");
        }

        if (request.getStatus() != ServiceRequest.RequestStatus.PENDING) {
            throw new BadRequestException("Request is not in PENDING status");
        }

        request.setStatus(ServiceRequest.RequestStatus.REJECTED);
        request = serviceRequestRepository.save(request);
        return convertToDto(request);
    }

    @Transactional
    public ServiceRequestDto completeRequest(Long requestId) {
        User entrepreneur = getCurrentUser();
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found"));

        if (!request.getEntrepreneur().getId().equals(entrepreneur.getId())) {
            throw new UnauthorizedException("You can only complete your own requests");
        }

        if (request.getStatus() != ServiceRequest.RequestStatus.ACCEPTED) {
            throw new BadRequestException("Request must be ACCEPTED before completion");
        }

        request.setStatus(ServiceRequest.RequestStatus.COMPLETED);
        request = serviceRequestRepository.save(request);
        return convertToDto(request);
    }

    public List<OrderDto> getMyOrders() {
        User entrepreneur = getCurrentUser();
        List<Order> orders = orderRepository.findByProductEntrepreneur(entrepreneur);
        orders.sort((o1, o2) -> {
            if (o1.getOrderDate() == null && o2.getOrderDate() == null) return 0;
            if (o1.getOrderDate() == null) return 1;
            if (o2.getOrderDate() == null) return -1;
            return o2.getOrderDate().compareTo(o1.getOrderDate());
        });
        return orders.stream().map(this::convertToOrderDto).collect(Collectors.toList());
    }

    public PageResponse<OrderDto> getMyOrders(int page, int size) {
        User entrepreneur = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<Order> orders = orderRepository.findByProductEntrepreneur(entrepreneur, pageable);

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

    public Double getEarningsSummary() {
        User entrepreneur = getCurrentUser();
        EntrepreneurProfile profile = entrepreneurProfileRepository.findByUser(entrepreneur)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepreneur profile not found"));
        return profile.getEarnings();
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
        dto.setArtisanBrandName(product.getArtisanBrandName());
        dto.setArtisanProductType(product.getArtisanProductType());
        dto.setArtisanMakingProcess(product.getArtisanMakingProcess());
        dto.setArtisanUniqueFeatures(product.getArtisanUniqueFeatures());
        dto.setArtisanDimensions(product.getArtisanDimensions());
        dto.setArtisanWeight(product.getArtisanWeight());
        dto.setArtisanMaterial(product.getArtisanMaterial());
        dto.setArtisanColorVariants(product.getArtisanColorVariants());
        dto.setArtisanQuantityType(product.getArtisanQuantityType());
        dto.setArtisanDiscount(product.getArtisanDiscount());
        dto.setArtisanTaxesIncluded(product.getArtisanTaxesIncluded());
        dto.setArtisanShippingCost(product.getArtisanShippingCost());
        dto.setArtisanStockQuantity(product.getArtisanStockQuantity());
        dto.setArtisanStockMode(product.getArtisanStockMode());
        dto.setArtisanRestockTimeline(product.getArtisanRestockTimeline());
        dto.setArtisanShippingLocations(product.getArtisanShippingLocations());
        dto.setArtisanCourierPartner(product.getArtisanCourierPartner());
        dto.setArtisanReturnWindow(product.getArtisanReturnWindow());
        dto.setArtisanReturnConditions(product.getArtisanReturnConditions());
        dto.setArtisanRefundReplacementDetails(product.getArtisanRefundReplacementDetails());
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
        dto.setProductId(order.getProduct().getId());
        dto.setProductName(order.getProduct().getName());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }
}
