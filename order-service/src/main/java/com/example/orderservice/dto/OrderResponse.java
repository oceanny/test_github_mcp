package com.example.orderservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 주문 응답 DTO */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  private Long id;
  private Long userId;
  private Long productId;
  private Integer quantity;
  private String status;
  private LocalDateTime createdAt;
}
