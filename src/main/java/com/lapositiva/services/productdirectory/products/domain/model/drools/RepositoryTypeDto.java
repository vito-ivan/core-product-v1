package com.lapositiva.services.productdirectory.products.domain.model.drools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class that contains rule id and validations.
 *
 * @author vito
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryTypeDto {

  private String inboundCode;
  private String outboundCode;

}
