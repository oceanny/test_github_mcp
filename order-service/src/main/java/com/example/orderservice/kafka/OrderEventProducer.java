package com.example.orderservice.kafka;

import com.example.common.event.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** 주문 생성 이벤트 Producer */
@Component
public class OrderEventProducer {
  private static final String TOPIC = "order-created-events";

  @Autowired private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

  public void publishOrderCreatedEvent(OrderCreatedEvent event) {
    kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
  }
}
