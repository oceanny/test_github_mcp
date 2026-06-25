package com.example.productservice.controller;

import com.example.common.dto.ApiResponse;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 상품 컨트롤러 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired private ProductService productService;

  @PostMapping
  public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
      @RequestBody CreateProductRequest request) {
    ProductResponse response = productService.createProduct(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Product created successfully", response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
    ProductResponse response = productService.getProductById(id);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
    List<ProductResponse> responses = productService.getAllProducts();
    return ResponseEntity.ok(ApiResponse.success(responses));
  }

  @PatchMapping("/{id}/stock/decrease")
  public ResponseEntity<ApiResponse<ProductResponse>> decreaseStock(
      @PathVariable Long id, @RequestParam Integer quantity, @RequestParam(defaultValue = "MANUAL") String reason) {
    ProductResponse response = productService.decreaseStock(id, quantity, reason);
    return ResponseEntity.ok(ApiResponse.success("Stock decreased successfully", response));
  }
}
