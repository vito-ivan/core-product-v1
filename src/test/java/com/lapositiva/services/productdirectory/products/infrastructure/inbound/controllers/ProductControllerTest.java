package com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers;


import com.lapositiva.services.productdirectory.products.application.create.ProductCreateUseCase;
import com.lapositiva.services.productdirectory.products.domain.model.PostProductResponse;
import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.model.ProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  @Mock
  private ProductCreateUseCase productCreateUseCase;

  @InjectMocks
  private ProductController productController;

  @Test
  @DisplayName("Return http status 201 when the use-case returns the created product")
  void returnHttpStatus201WhenTheUseCaseReturnsTheCreatedProduct() {

    // Arrange
    String repositoryTypeCode = "mongodb";
    Mono<ProductRequest> productRequest = Mono.just(new ProductRequest().code("D344").name("Product Name"));
    var productDto = ProductDto.builder()
        .id("123e4567-e89b-12d3-a456-426614174000")
        .code("D344")
        .name("Product Name")
        .repositoryTypeCode(repositoryTypeCode)
        .build();

    var postProductResponse = new PostProductResponse().id("123e4567-e89b-12d3-a456-426614174000");

    when(productCreateUseCase.create(any(ProductDto.class))).thenReturn(Mono.just(productDto));

    // Act
    var result = productController.create(repositoryTypeCode, productRequest, null);

    // Assert
    StepVerifier.create(result)
        .assertNext(responseEntity -> {
          Assertions.assertNotNull(responseEntity);
          Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
          Assertions.assertEquals(postProductResponse.getId(), responseEntity.getBody().getId());
        })
        .expectComplete()
        .verify();
  }
}