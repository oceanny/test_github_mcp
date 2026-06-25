package com.example.productservice.kafka;

import com.example.common.event.StockUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** 재고 업데이트 이벤트 Producer */
@Component
public class ProductEventProducer {
  private static final String TOPIC = "stock-updated-events";

  @Autowired private KafkaTemplate<String, StockUpdatedEvent> kafkaTemplate;

  public void publishStockUpdatedEvent(StockUpdatedEvent event) {
    kafkaTemplate.send(TOPIC, event.getProductId().toString(), event);
  }
}
