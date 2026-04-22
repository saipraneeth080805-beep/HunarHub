package com.hunarhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDisputeRequest {
    /**
     * Email or username (name) of the user the complaint is against.
     * This is required for filing a complaint so the system can notify them.
     */
    @NotBlank(message = "Reported user's email/username is required")
    private String reportedUserIdentifier;

    // Backward compatible (older clients might still send an id)
    private Long reportedUserId;
    private Long orderId;
    private Long serviceRequestId;
    
    @NotBlank(message = "Dispute type is required")
    private String disputeType;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
}
