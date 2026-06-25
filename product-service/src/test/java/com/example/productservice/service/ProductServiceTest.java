package com.example.productservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.common.event.StockUpdatedEvent;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.kafka.ProductEventProducer;
import com.example.productservice.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock private ProductRepository productRepository;

  @Mock private ProductEventProducer productEventProducer;

  @InjectMocks private ProductService productService;

  private CreateProductRequest createProductRequest;

  @BeforeEach
  void setUp() {
    createProductRequest =
        CreateProductRequest.builder()
            .name("iPhone 15")
            .price(new BigDecimal("1200.00"))
            .stock(100)
            .build();
  }

  @Test
  void should_createProduct_when_validRequest() {
    Product product =
        Product.builder()
            .id(1L)
            .name("iPhone 15")
            .price(new BigDecimal("1200.00"))
            .stock(100)
            .createdAt(LocalDateTime.now())
            .build();

    when(productRepository.save(any(Product.class))).thenReturn(product);

    ProductResponse response = productService.createProduct(createProductRequest);

    assertNotNull(response);
    assertEquals("iPhone 15", response.getName());
    verify(productRepository).save(any(Product.class));
  }

  @Test
  void should_decreaseStock_when_sufficientStock() {
    Long productId = 1L;
    Product product =
        Product.builder()
            .id(productId)
            .name("iPhone 15")
            .price(new BigDecimal("1200.00"))
            .stock(100)
            .createdAt(LocalDateTime.now())
            .build();

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ProductResponse response = productService.decreaseStock(productId, 10, "ORDER");

    assertNotNull(response);
    assertEquals(90, response.getStock());
    verify(productEventProducer).publishStockUpdatedEvent(any(StockUpdatedEvent.class));
  }

  @Test
  void should_throwInsufficientStockException_when_stockIsLow() {
    Long productId = 1L;
    Product product =
        Product.builder()
            .id(productId)
            .name("iPhone 15")
            .price(new BigDecimal("1200.00"))
            .stock(5)
            .createdAt(LocalDateTime.now())
            .build();

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    assertThrows(
      IllegalArgumentException.class, () -> productService.decreaseStock(productId, 10, "ORDER"));
  }

  @Test
  void should_throwProductNotFoundException_when_productNotFound() {
    Long productId = 999L;
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> productService.getProductById(productId));
  }
}
