
package com.lapositiva.services.productdirectory.products.application.create;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.repository.ProductCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Product Create Use Case.
 *
 * @author Vito
 */
@Service
@RequiredArgsConstructor
public class ProductCreateUseCase {

  private final ProductCommandRepository productCommandRepository;

  /**
   * Create a product.
   *
   * @param productDto the product to create.
   * @return the created product.
   */
  public Mono<ProductDto> create(final ProductDto productDto) {
    return productCommandRepository
        .create(productDto);
  }

  /**
   * Update a product.
   *
   * @param productDto the product to update.
   * @return the updated product.
   */
  public Mono<ProductDto> update(final ProductDto productDto) {
    return productCommandRepository
        .update(productDto);
  }

  /**
   * Delete a product.
   *
   * @param productDto the product to delete.
   * @return the deleted product.
   */
  public Mono<Void> delete(final ProductDto productDto) {
    return productCommandRepository
        .delete(productDto);
  }
}
