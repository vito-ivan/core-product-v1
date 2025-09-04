package com.lapositiva.services.productdirectory.products.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Product Dto.
 * This class is used to represent a product.
 */
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
  private String id;

  private String code;
  private String name;

  private String repositoryTypeCode;
}

