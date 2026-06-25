package com.example.orderservice.kafka;

import com.example.common.event.StockUpdatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** 재고 업데이트 이벤트 Consumer */
@Component
public class OrderEventConsumer {

  @KafkaListener(topics = "stock-updated-events", groupId = "order-service-group")
  public void consumeStockUpdatedEvent(StockUpdatedEvent event) {
    // 현재 구현에서는 로깅/확장 포인트 용도로 이벤트를 수신한다.
    // TODO: 재고 업데이트 결과에 따라 주문 상태를 CONFIRMED/FAILED로 변경하는 로직 추가 가능.
  }
}
