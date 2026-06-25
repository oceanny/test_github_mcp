package com.example.orderservice.controller;

import com.example.common.dto.ApiResponse;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 주문 컨트롤러 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired private OrderService orderService;

  @PostMapping
  public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody CreateOrderRequest request) {
    OrderResponse response = orderService.createOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Order created successfully", response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
    OrderResponse response = orderService.getOrderById(id);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
