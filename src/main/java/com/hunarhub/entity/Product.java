package com.hunarhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    // Comma-separated list of additional image URLs (for galleries)
    @Column(name = "additional_image_urls", columnDefinition = "TEXT")
    private String additionalImageUrls;

    @ManyToOne
    @JoinColumn(name = "entrepreneur_id", nullable = false)
    private User entrepreneur;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    // Comma-separated list of available sizes, e.g. "S,M,L"
    @Column(name = "available_sizes")
    private String availableSizes;

    // Tailor / apparel specific metadata
    private String fabric;
    private String color;
    private String fit;
    @Column(name = "wash_care")
    private String washCare;
    @Column(name = "style_notes", columnDefinition = "TEXT")
    private String styleNotes;
    private String spec1;
    private String spec2;

    // Potter-specific metadata
    @Column(name = "quantity_available")
    private Integer quantityAvailable;
    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;
    @Column(name = "potter_product_type")
    private String potterProductType;
    @Column(name = "detailed_description", columnDefinition = "TEXT")
    private String detailedDescription;
    private String height;
    @Column(name = "diameter_top")
    private String diameterTop;
    @Column(name = "diameter_bottom")
    private String diameterBottom;
    private String capacity;
    private String material;
    @Column(name = "finish_type")
    private String finishType;
    @Column(name = "handmade_or_machine")
    private String handmadeOrMachine;
    @Column(name = "suitable_for")
    private String suitableFor;
    @Column(name = "usage_environment")
    private String usageEnvironment;
    @Column(name = "food_safe")
    private Boolean foodSafe;
    @Column(name = "drainage_hole")
    private Boolean drainageHole;
    @Column(name = "weight_approx")
    private String weightApprox;
    private Boolean fragile;
    @Column(name = "handling_instructions", columnDefinition = "TEXT")
    private String handlingInstructions;
    @Column(name = "delivery_time")
    private String deliveryTime;
    @Column(name = "packaging_details", columnDefinition = "TEXT")
    private String packagingDetails;
    @Column(name = "handmade_by_artisan")
    private Boolean handmadeByArtisan;
    @Column(name = "origin_story", columnDefinition = "TEXT")
    private String originStory;
    @Column(name = "eco_friendly")
    private Boolean ecoFriendly;
    @Column(name = "return_policy", columnDefinition = "TEXT")
    private String returnPolicy;
    @Column(name = "replacement_policy", columnDefinition = "TEXT")
    private String replacementPolicy;

    // Artisan-specific metadata
    @Column(name = "artisan_brand_name")
    private String artisanBrandName;
    @Column(name = "artisan_product_type")
    private String artisanProductType;
    @Column(name = "artisan_making_process", columnDefinition = "TEXT")
    private String artisanMakingProcess;
    @Column(name = "artisan_unique_features", columnDefinition = "TEXT")
    private String artisanUniqueFeatures;
    @Column(name = "artisan_dimensions")
    private String artisanDimensions;
    @Column(name = "artisan_weight")
    private String artisanWeight;
    @Column(name = "artisan_material")
    private String artisanMaterial;
    @Column(name = "artisan_color_variants")
    private String artisanColorVariants;
    @Column(name = "artisan_quantity_type")
    private String artisanQuantityType;
    @Column(name = "artisan_discount")
    private String artisanDiscount;
    @Column(name = "artisan_taxes_included")
    private Boolean artisanTaxesIncluded;
    @Column(name = "artisan_shipping_cost")
    private String artisanShippingCost;
    @Column(name = "artisan_stock_quantity")
    private Integer artisanStockQuantity;
    @Column(name = "artisan_stock_mode")
    private String artisanStockMode;
    @Column(name = "artisan_restock_timeline")
    private String artisanRestockTimeline;
    @Column(name = "artisan_shipping_locations")
    private String artisanShippingLocations;
    @Column(name = "artisan_courier_partner")
    private String artisanCourierPartner;
    @Column(name = "artisan_return_window")
    private String artisanReturnWindow;
    @Column(name = "artisan_return_conditions", columnDefinition = "TEXT")
    private String artisanReturnConditions;
    @Column(name = "artisan_refund_replacement_details", columnDefinition = "TEXT")
    private String artisanRefundReplacementDetails;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
