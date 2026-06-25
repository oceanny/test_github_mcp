package com.example.userservice.kafka;

import com.example.common.event.UserCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/** 사용자 생성 이벤트 Producer */
@Component
public class UserEventProducer {
  private static final String TOPIC = "user-created-events";

  @Autowired private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

  public void publishUserCreatedEvent(UserCreatedEvent event) {
    kafkaTemplate.send(TOPIC, event.getUserId().toString(), event);
  }
}
