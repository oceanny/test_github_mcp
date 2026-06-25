package com.example.productservice.service;

import com.example.common.event.StockUpdatedEvent;
import com.example.productservice.dto.CreateProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.kafka.ProductEventProducer;
import com.example.productservice.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 상품 서비스 */
@Service
public class ProductService {

  @Autowired private ProductRepository productRepository;

  @Autowired private ProductEventProducer productEventProducer;

  @Transactional
  public ProductResponse createProduct(CreateProductRequest request) {
    Product product =
        Product.builder()
            .name(request.getName())
            .price(request.getPrice())
            .stock(request.getStock())
            .build();

    Product savedProduct = productRepository.save(product);
    return convertToResponse(savedProduct);
  }

  @Transactional(readOnly = true)
  public ProductResponse getProductById(Long id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new IllegalStateException("Product not found with id: " + id));
    return convertToResponse(product);
  }

  @Transactional(readOnly = true)
  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public ProductResponse decreaseStock(Long productId, Integer quantity, String reason) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new IllegalStateException("Product not found with id: " + productId));

    if (product.getStock() < quantity) {
      throw new IllegalArgumentException("Insufficient stock for product id: " + productId);
    }

    Integer previousStock = product.getStock();
    product.setStock(previousStock - quantity);
    Product savedProduct = productRepository.save(product);

    StockUpdatedEvent event =
        StockUpdatedEvent.builder()
            .productId(savedProduct.getId())
            .productName(savedProduct.getName())
            .previousStock(previousStock)
            .currentStock(savedProduct.getStock())
            .changeAmount(-quantity)
            .reason(reason)
            .updatedAt(LocalDateTime.now())
            .build();

    productEventProducer.publishStockUpdatedEvent(event);
    return convertToResponse(savedProduct);
  }

  private ProductResponse convertToResponse(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .stock(product.getStock())
        .createdAt(product.getCreatedAt())
        .build();
  }
}
