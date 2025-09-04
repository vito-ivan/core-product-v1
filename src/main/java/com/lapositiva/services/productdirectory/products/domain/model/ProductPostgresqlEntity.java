package com.lapositiva.services.productdirectory.products.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Product Entity.
 * This class is used to represent a product.
 */
@Table("prod_dire.product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPostgresqlEntity {
  @Id
  private String id;
  @Column("code")
  private String code;
  @Column("name")
  private String name;

}
