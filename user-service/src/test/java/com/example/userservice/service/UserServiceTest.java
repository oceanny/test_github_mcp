package com.example.userservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.common.event.UserCreatedEvent;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.kafka.UserEventProducer;
import com.example.userservice.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserEventProducer userEventProducer;

  @InjectMocks private UserService userService;

  private CreateUserRequest createUserRequest;

  @BeforeEach
  void setUp() {
    createUserRequest =
        CreateUserRequest.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .build();
  }

  @Test
  void should_createUser_when_validRequest() {
    User user =
        User.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .createdAt(LocalDateTime.now())
            .build();

    when(userRepository.findByEmail(createUserRequest.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserResponse response = userService.createUser(createUserRequest);

    assertNotNull(response);
    assertEquals("testuser", response.getUsername());
    assertEquals("test@example.com", response.getEmail());
    verify(userRepository).save(any(User.class));
    verify(userEventProducer).publishUserCreatedEvent(any(UserCreatedEvent.class));
  }

  @Test
  void should_throwUserAlreadyExistsException_when_emailExists() {
    User existingUser =
        User.builder()
            .id(1L)
            .username("existinguser")
            .email("test@example.com")
            .password("password123")
            .build();

    when(userRepository.findByEmail(createUserRequest.getEmail()))
        .thenReturn(Optional.of(existingUser));

    assertThrows(
      IllegalArgumentException.class, () -> userService.createUser(createUserRequest));
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void should_getUserById_when_userExists() {
    Long userId = 1L;
    User user =
        User.builder()
            .id(userId)
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .createdAt(LocalDateTime.now())
            .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserResponse response = userService.getUserById(userId);

    assertNotNull(response);
    assertEquals(userId, response.getId());
    assertEquals("testuser", response.getUsername());
  }

  @Test
  void should_throwUserNotFoundException_when_userNotFound() {
    Long userId = 999L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userService.getUserById(userId));
  }
}
