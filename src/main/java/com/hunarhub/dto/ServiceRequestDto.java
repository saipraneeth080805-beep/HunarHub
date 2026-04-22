package com.hunarhub.dto;

import com.hunarhub.entity.ServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long entrepreneurId;
    private String entrepreneurName;
    private String serviceDescription;
    private ServiceRequest.RequestStatus status;
    private LocalDateTime requestedDate;
    private LocalDateTime scheduledDate;
}
