package com.example.orderservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.common.event.OrderCreatedEvent;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.kafka.OrderEventProducer;
import com.example.orderservice.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock private OrderRepository orderRepository;

  @Mock private OrderEventProducer orderEventProducer;

  @InjectMocks private OrderService orderService;

  private CreateOrderRequest createOrderRequest;

  @BeforeEach
  void setUp() {
    createOrderRequest = CreateOrderRequest.builder().userId(1L).productId(10L).quantity(2).build();
  }

  @Test
  void should_createOrder_when_validRequest() {
    Order order =
        Order.builder()
            .id(100L)
            .userId(1L)
            .productId(10L)
            .quantity(2)
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .build();

    when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderResponse response = orderService.createOrder(createOrderRequest);

    assertNotNull(response);
    assertEquals(100L, response.getId());
    assertEquals("PENDING", response.getStatus());
    verify(orderEventProducer).publishOrderCreatedEvent(any(OrderCreatedEvent.class));
  }

  @Test
  void should_getOrderById_when_orderExists() {
    Long orderId = 100L;
    Order order =
        Order.builder()
            .id(orderId)
            .userId(1L)
            .productId(10L)
            .quantity(2)
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .build();

    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    OrderResponse response = orderService.getOrderById(orderId);

    assertNotNull(response);
    assertEquals(orderId, response.getId());
  }

  @Test
  void should_throwOrderNotFoundException_when_orderNotFound() {
    Long orderId = 999L;
    when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> orderService.getOrderById(orderId));
  }
}
