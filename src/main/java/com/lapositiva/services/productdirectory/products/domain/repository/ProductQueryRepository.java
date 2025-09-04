package com.lapositiva.services.productdirectory.products.domain.repository;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Product Query Repository.
 * This interface is used to interact with the database.
 * It contains the methods to find a product by id or all products.
 * @see ProductDto
 */
public interface ProductQueryRepository {

  /**
   * Find a product by id.
   *
   * @param productDto the product to find.
   * @return the found product.
   */
  Mono<ProductDto> findById(ProductDto productDto);

  /**
   * Find all products.
   *
   * @param productDto the product to find.
   * @return the found products.
   */
  Flux<ProductDto> findAll(ProductDto productDto);
}