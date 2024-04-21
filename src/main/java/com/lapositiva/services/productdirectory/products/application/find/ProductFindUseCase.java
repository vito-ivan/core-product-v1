package com.lapositiva.services.productdirectory.products.application.find;

import com.lapositiva.services.productdirectory.products.domain.model.ProductDto;
import com.lapositiva.services.productdirectory.products.domain.repository.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Product Find Use Case.
 * This class is used to find a product by id or all products.
 *
 * @see ProductDto
 * @see ProductQueryRepository
 */
@Service
@RequiredArgsConstructor
public class ProductFindUseCase {

  private final ProductQueryRepository productQueryRepository;

  /**
   * Find all products.
   *
   * @param productDto the product to find.
   * @return the found products.
   */
  public Flux<ProductDto> findAll(final ProductDto productDto) {
    return productQueryRepository
        .findAll(productDto);
  }

  /**
   * Find a product by id.
   *
   * @param productDto the product to find.
   * @return the found product.
   */
  public Mono<ProductDto> findById(final ProductDto productDto) {
    return productQueryRepository
        .findById(productDto);
  }
}