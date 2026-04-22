package com.hunarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String additionalImageUrls;
    private Long entrepreneurId;
    private String entrepreneurName;
    private Long categoryId;
    private String categoryName;
    private Boolean available;
    private LocalDateTime createdAt;
    private String availableSizes;
    private String fabric;
    private String color;
    private String fit;
    private String washCare;
    private String styleNotes;
    private String spec1;
    private String spec2;
    private Integer quantityAvailable;
    private String shortDescription;
    private String potterProductType;
    private String detailedDescription;
    private String height;
    private String diameterTop;
    private String diameterBottom;
    private String capacity;
    private String material;
    private String finishType;
    private String handmadeOrMachine;
    private String suitableFor;
    private String usageEnvironment;
    private Boolean foodSafe;
    private Boolean drainageHole;
    private String weightApprox;
    private Boolean fragile;
    private String handlingInstructions;
    private String deliveryTime;
    private String packagingDetails;
    private Boolean handmadeByArtisan;
    private String originStory;
    private Boolean ecoFriendly;
    private String returnPolicy;
    private String replacementPolicy;
    private String artisanBrandName;
    private String artisanProductType;
    private String artisanMakingProcess;
    private String artisanUniqueFeatures;
    private String artisanDimensions;
    private String artisanWeight;
    private String artisanMaterial;
    private String artisanColorVariants;
    private String artisanQuantityType;
    private String artisanDiscount;
    private Boolean artisanTaxesIncluded;
    private String artisanShippingCost;
    private Integer artisanStockQuantity;
    private String artisanStockMode;
    private String artisanRestockTimeline;
    private String artisanShippingLocations;
    private String artisanCourierPartner;
    private String artisanReturnWindow;
    private String artisanReturnConditions;
    private String artisanRefundReplacementDetails;
}
