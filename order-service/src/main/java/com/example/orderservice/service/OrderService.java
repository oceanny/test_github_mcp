package com.example.orderservice.service;

import com.example.common.event.OrderCreatedEvent;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.kafka.OrderEventProducer;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 주문 서비스 */
@Service
public class OrderService {

  @Autowired private OrderRepository orderRepository;

  @Autowired private OrderEventProducer orderEventProducer;

  @Transactional
  public OrderResponse createOrder(CreateOrderRequest request) {
    Order order =
        Order.builder()
            .userId(request.getUserId())
            .productId(request.getProductId())
            .quantity(request.getQuantity())
            .status("PENDING")
            .build();

    Order savedOrder = orderRepository.save(order);

    OrderCreatedEvent event =
        OrderCreatedEvent.builder()
            .orderId(savedOrder.getId())
            .userId(savedOrder.getUserId())
            .productId(savedOrder.getProductId())
            .quantity(savedOrder.getQuantity())
            .createdAt(savedOrder.getCreatedAt())
            .build();

    orderEventProducer.publishOrderCreatedEvent(event);
    return convertToResponse(savedOrder);
  }

  @Transactional(readOnly = true)
  public OrderResponse getOrderById(Long id) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new IllegalStateException("Order not found with id: " + id));
    return convertToResponse(order);
  }

  private OrderResponse convertToResponse(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .userId(order.getUserId())
        .productId(order.getProductId())
        .quantity(order.getQuantity())
        .status(order.getStatus())
        .createdAt(order.getCreatedAt())
        .build();
  }
}
