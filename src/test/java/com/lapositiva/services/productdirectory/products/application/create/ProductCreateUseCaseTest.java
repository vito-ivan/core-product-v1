package com.lapositiva.services.productdirectory.products.application.create;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.repository.ProductCommandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCreateUseCaseTest {

  @Mock
  private ProductCommandRepository productCommandRepository;

  @InjectMocks
  private ProductCreateUseCase productCreateUseCase;

  @Test
  @DisplayName("Return ProductDto when Repository returns ProductDto")
  void returnsProductDtoWhenRepositoryReturnsProductDto() {

    // Arrange
    var productDto = ProductDto.builder()
        .id("123e4567-e89b-12d3-a456-426614174000")
        .code("D344")
        .name("Product Name")
        .repositoryTypeCode("mongodb")
        .build();

    when(productCommandRepository.create(any(ProductDto.class))).thenReturn(Mono.just(productDto));

    // Act
    var result = productCreateUseCase.create(productDto);

    // Assert
    StepVerifier.create(result)
        .assertNext(returnedProductDto -> {
          Assertions.assertNotNull(returnedProductDto);
          Assertions.assertEquals(productDto.getId(), returnedProductDto.getId());
        })
        .expectComplete()
        .verify();
  }
}