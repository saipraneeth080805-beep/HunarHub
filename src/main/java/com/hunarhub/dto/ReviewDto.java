package com.hunarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private Integer rating;
    private String comment;
    private Long customerId;
    private String customerName;
    private Long entrepreneurId;
    private String entrepreneurName;
    private LocalDateTime createdAt;
}
