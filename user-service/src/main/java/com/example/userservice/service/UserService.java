package com.example.userservice.service;

import com.example.common.event.UserCreatedEvent;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.kafka.UserEventProducer;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 사용자 서비스 */
@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  @Autowired private UserEventProducer userEventProducer;

  @Transactional
  public UserResponse createUser(CreateUserRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already exists: " + request.getEmail());
    }

    User user =
        User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(request.getPassword())
            .build();

    User savedUser = userRepository.save(user);

    UserCreatedEvent event =
        UserCreatedEvent.builder()
            .userId(savedUser.getId())
            .username(savedUser.getUsername())
            .email(savedUser.getEmail())
            .createdAt(savedUser.getCreatedAt())
            .build();

    userEventProducer.publishUserCreatedEvent(event);

    return convertToResponse(savedUser);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalStateException("User not found with id: " + id));
    return convertToResponse(user);
  }

  private UserResponse convertToResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .build();
  }
}
