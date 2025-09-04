package com.lapositiva.services.productdirectory.products.infrastructure;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.model.ProductEntity;
import com.lapositiva.services.productdirectory.products.domain.model.ProductPostgresqlEntity;
import com.lapositiva.services.productdirectory.products.domain.model.drools.RepositoryTypeDto;
import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.exception.ApiException;
import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.utils.PropertyUtils;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.ProductMongoDdRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.ProductPostgresqlRepository;
import com.lapositiva.services.productdirectory.products.infrastructure.outbound.drools.DroolsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveInsertOperation;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory.EXTERNAL_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCommandRepositoryImplTest {

  @Mock
  private Environment environment;
  @Mock
  private R2dbcEntityTemplate entityTemplate;
  @Mock
  private DroolsService droolsService;

  @Mock
  private ProductMongoDdRepository productMongoDdRepository;

  @Mock
  private ProductPostgresqlRepository productPostgreSqlRepository;

  @InjectMocks
  private ProductCommandRepositoryImpl productCommandRepositoryImpl;

  @Mock
  private ReactiveInsertOperation.ReactiveInsert<ProductPostgresqlEntity> insertMock;

  @Nested
  class SuccessCases {

    @Test
    @DisplayName("Return ProductDto when Drool Service returns MongoDb and Repository returns ProductDto")
    void returnsProductDtoWhenDroolServiceReturnsMongoDbAndRepositoryReturnsProductDto() {

      // Arrange
      var productDto = ProductDto.builder()
          .id("123e4567-e89b-12d3-a456-426614174000")
          .code("D344")
          .name("Product Name")
          .repositoryTypeCode("mongodb")
          .build();

      var repositoryTypeDto = RepositoryTypeDto.builder().outboundCode("mongodb").inboundCode("mongodb").build();

      when(droolsService.executeRules(any(RepositoryTypeDto.class))).thenReturn(repositoryTypeDto);
      when(productMongoDdRepository.save(any(ProductEntity.class))).thenReturn(Mono.just(ProductEntity.builder()
          .id("123e4567-e89b-12d3-a456-426614174000")
          .code("D344")
          .name("Product Name").build()));

      // Act
      var result = productCommandRepositoryImpl.create(productDto);

      // Assert
      StepVerifier.create(result)
          .assertNext(returnedProductDto -> {
            Assertions.assertNotNull(returnedProductDto);
            Assertions.assertEquals(productDto.getId(), returnedProductDto.getId());
            Assertions.assertEquals(productDto.getCode(), returnedProductDto.getCode());
            Assertions.assertEquals(productDto.getName(), returnedProductDto.getName());
            Assertions.assertEquals(productDto.getRepositoryTypeCode(), returnedProductDto.getRepositoryTypeCode());
          })
          .expectComplete()
          .verify();
    }

    @Test
    @DisplayName("Return ProductDto when Drool Service returns Postgresql and Repository returns ProductDto")
    void returnsProductDtoWhenDroolServiceReturnsPostgresqlAndRepositoryReturnsProductDto() {

      // Arrange
      var productDto = ProductDto.builder()
          .id("123e4567-e89b-12d3-a456-426614174000")
          .code("D344")
          .name("Product Name")
          .repositoryTypeCode("postgresql")
          .build();

      var repositoryTypeDto = RepositoryTypeDto.builder().outboundCode("postgresql").inboundCode("postgresql").build();


      when(droolsService.executeRules(any(RepositoryTypeDto.class))).thenReturn(repositoryTypeDto);

      when(entityTemplate.insert(ProductPostgresqlEntity.class)).thenReturn(insertMock);

      when(insertMock.using(any(ProductPostgresqlEntity.class)))
          .thenReturn(Mono.just(ProductPostgresqlEntity.builder()
              .id("123e4567-e89b-12d3-a456-426614174000")
              .code("D344")
              .name("Product Name").build()));

      // Act
      var result = productCommandRepositoryImpl.create(productDto);

      // Assert
      StepVerifier.create(result)
          .assertNext(returnedProductDto -> {
            Assertions.assertNotNull(returnedProductDto);
            Assertions.assertEquals(productDto.getId(), returnedProductDto.getId());
            Assertions.assertEquals(productDto.getCode(), returnedProductDto.getCode());
            Assertions.assertEquals(productDto.getName(), returnedProductDto.getName());
            Assertions.assertEquals(productDto.getRepositoryTypeCode(), returnedProductDto.getRepositoryTypeCode());
          })
          .expectComplete()
          .verify();
    }
  }

  @Nested
  class ErrorCases {

    @Test
    @DisplayName("Return Error when MongoDb Repository throws exception")
    void returnsErrorWhenMongoDbRepositoryThrowsException() {

      PropertyUtils.setResolver(environment);

      given(environment.getProperty("application.api.error-code.external-error.code")).willReturn("T0099");
      given(environment.getProperty("application.api.error-code.external-error.description")).willReturn(
          "Internal server error");
      given(environment.getProperty("application.api.error-code.external-error.error-type")).willReturn("Technical");

      // Arrange
      var productDto = ProductDto.builder()
          .id("123e4567-e89b-12d3-a456-426614174000")
          .code("D344")
          .name("Product Name")
          .repositoryTypeCode("mongodb")
          .build();

      var repositoryTypeDto = RepositoryTypeDto.builder()
          .outboundCode("mongodb")
          .inboundCode("mongodb")
          .build();

      Mockito.when(droolsService.executeRules(any(RepositoryTypeDto.class))).thenReturn(repositoryTypeDto);
      Mockito.when(productMongoDdRepository.save(any(ProductEntity.class)))
          .thenReturn(Mono.error(new RuntimeException("MongoDB repository error")));

      // Act
      var result = productCommandRepositoryImpl.create(productDto);

      // Assert
      StepVerifier.create(result)
          .expectErrorMatches(throwable -> throwable instanceof ApiException
              && EXTERNAL_ERROR.equals(checkAndCast(throwable).getCategory()))
          .verify();
    }

    @Test
    @DisplayName("Return Error when PostgreSql Repository throws exception")
    void returnsErrorWhenPostgreSqlRepositoryThrowsException() {

      PropertyUtils.setResolver(environment);

      given(environment.getProperty("application.api.error-code.external-error.code")).willReturn("T0099");
      given(environment.getProperty("application.api.error-code.external-error.description")).willReturn(
          "Internal server error");
      given(environment.getProperty("application.api.error-code.external-error.error-type")).willReturn("Technical");

      // Arrange
      var productDto = ProductDto.builder()
          .id("123e4567-e89b-12d3-a456-426614174000")
          .code("D344")
          .name("Product Name")
          .repositoryTypeCode("postgresql")
          .build();

      var repositoryTypeDto = RepositoryTypeDto.builder()
          .outboundCode("postgresql")
          .inboundCode("postgresql")
          .build();

      when(droolsService.executeRules(any(RepositoryTypeDto.class))).thenReturn(repositoryTypeDto);

      when(entityTemplate.insert(ProductPostgresqlEntity.class)).thenReturn(insertMock);

      when(insertMock.using(any(ProductPostgresqlEntity.class)))
          .thenReturn(Mono.error(new RuntimeException("PostgreSql repository error")));

      // Act
      var result = productCommandRepositoryImpl.create(productDto);

      // Assert
      StepVerifier.create(result)
          .expectErrorMatches(throwable -> throwable instanceof ApiException
              && EXTERNAL_ERROR.equals(checkAndCast(throwable).getCategory()))
          .verify();
    }

    private static <T> ApiException checkAndCast(T throwable) {
      if (throwable instanceof ApiException apiException) {
        return apiException;
      }
      return null;
    }
  }

}