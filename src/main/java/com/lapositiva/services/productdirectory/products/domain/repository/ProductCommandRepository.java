package com.lapositiva.services.productdirectory.products.domain.repository;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import reactor.core.publisher.Mono;

/**
 * Product Command Repository.
 * This interface is used to interact with the database.
 * It contains the methods to create, update and delete a product.
 * @see ProductDto
 */
public interface ProductCommandRepository {

  /**
   * Create a product.
   *
   * @param productDto the product to create.
   * @return the created product.
   */
  Mono<ProductDto> create(ProductDto productDto);

  /**
   * Update a product.
   *
   * @param productDto the product to update.
   * @return the updated product.
   */
  Mono<ProductDto> update(ProductDto productDto);

  /**
   * Delete a product.
   *
   * @param productDto the product to delete.
   * @return the deleted product.
   */
  Mono<Void> delete(ProductDto productDto);
}