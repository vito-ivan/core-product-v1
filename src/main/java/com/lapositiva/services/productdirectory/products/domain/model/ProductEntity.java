package com.lapositiva.services.productdirectory.products.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Product Entity.
 * This class is used to represent a product.
 */
@Document("product")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
  @Id
  private String id;
  private String code;
  private String name;
}
