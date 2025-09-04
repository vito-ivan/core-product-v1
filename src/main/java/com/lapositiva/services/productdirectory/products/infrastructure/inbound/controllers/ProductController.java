package com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers;

import com.lapositiva.services.productdirectory.products.application.create.ProductCreateUseCase;
import com.lapositiva.services.productdirectory.products.application.find.ProductFindUseCase;
import com.lapositiva.services.productdirectory.products.domain.model.PostProductResponse;
import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.model.ProductRequest;
import com.lapositiva.services.productdirectory.products.domain.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Product Controller.
 * This class is used to handle the product requests.
 */
@RequiredArgsConstructor
@Component
public class ProductController implements ProductApiDelegate {

  private final ProductCreateUseCase productCreateUseCase;
  private final ProductFindUseCase productFindUseCase;

  @Override
  public Mono<ResponseEntity<PostProductResponse>> create(final String repositoryTypeCode,
                                                          final Mono<ProductRequest> productRequest,
                                                          final ServerWebExchange exchange) {

    return productRequest
        .flatMap(productRequest1 -> productCreateUseCase
            .create(buildProductDto(repositoryTypeCode, productRequest1)))
        .map(productDto -> new PostProductResponse().id(productDto.getId()))
        .map(postProductResponse -> ResponseEntity.status(HttpStatus.CREATED).body(postProductResponse));

  }

  private static ProductDto buildProductDto(final String repositoryTypeCode,
                                            final ProductRequest productRequest1) {

    return ProductDto.builder()
        .code(productRequest1.getCode())
        .name(productRequest1.getName())
        .repositoryTypeCode(repositoryTypeCode).build();
  }

  @Override
  public Mono<ResponseEntity<Void>> delete(final String repositoryTypeCode,
                                           final String id,
                                           final ServerWebExchange exchange) {

    return productCreateUseCase
        .delete(buildProductDto(repositoryTypeCode, id))
        .then(Mono.defer(() -> Mono.just(ResponseEntity.noContent().build())));

  }

  @Override
  public Mono<ResponseEntity<Void>> update(final String repositoryTypeCode,
                                           final String id,
                                           final Mono<ProductRequest> productRequest,
                                           final ServerWebExchange exchange) {

    return productRequest
        .flatMap(productRequest1 -> productCreateUseCase
            .update(buildProductDto(repositoryTypeCode, id, productRequest1)))
        .then(Mono.defer(() -> Mono.just(ResponseEntity.noContent().build())));

  }

  private static ProductDto buildProductDto(final String repositoryTypeCode,
                                            final String id,
                                            final ProductRequest productRequest1) {

    return ProductDto.builder()
        .id(id)
        .code(productRequest1.getCode())
        .name(productRequest1.getName())
        .repositoryTypeCode(repositoryTypeCode).build();
  }

  @Override
  public Mono<ResponseEntity<Flux<ProductResponse>>> findAll(final String repositoryTypeCode,
                                                             final ServerWebExchange exchange) {

    Flux<ProductResponse> productResponseFlux = productFindUseCase
        .findAll(ProductDto.builder()
            .repositoryTypeCode(repositoryTypeCode).build())
        .map(ProductController::buildProductResponse);

    return productResponseFlux.collectList()
        .flatMap(list -> list.isEmpty()
            ? Mono.just(ResponseEntity.noContent().build())
            : Mono.just(ResponseEntity.ok(productResponseFlux)));

  }


  @Override
  public Mono<ResponseEntity<ProductResponse>> findById(final String repositoryTypeCode,
                                                        final String id,
                                                        final ServerWebExchange exchange) {

    return productFindUseCase
        .findById(buildProductDto(repositoryTypeCode, id))
        .map(ProductController::buildProductResponse)
        .map(ResponseEntity::ok);

  }

  private static ProductDto buildProductDto(final String repositoryTypeCode, final String id) {

    return ProductDto.builder()
        .id(id)
        .repositoryTypeCode(repositoryTypeCode).build();
  }

  private static ProductResponse buildProductResponse(final ProductDto productQuery) {

    return new ProductResponse()
        .id(productQuery.getId())
        .code(productQuery.getCode())
        .name(productQuery.getName());
  }
}