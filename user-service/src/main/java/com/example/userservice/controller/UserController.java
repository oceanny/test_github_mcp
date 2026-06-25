package com.example.userservice.controller;

import com.example.common.dto.ApiResponse;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 사용자 컨트롤러 */
@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired private UserService userService;

  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
    UserResponse response = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("User created successfully", response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
    UserResponse response = userService.getUserById(id);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
