package com.example.productservice.kafka;

import com.example.common.event.OrderCreatedEvent;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** 주문 생성 이벤트 Consumer */
@Component
public class ProductEventConsumer {

  @Autowired private ProductService productService;

  @KafkaListener(topics = "order-created-events", groupId = "product-service-group")
  public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
    productService.decreaseStock(event.getProductId(), event.getQuantity(), "ORDER");
  }
}
