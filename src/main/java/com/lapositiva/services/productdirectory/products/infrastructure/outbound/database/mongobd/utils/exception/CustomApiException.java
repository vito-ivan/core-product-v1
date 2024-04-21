package com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.exception;

import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory;
import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

import static com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory.CONFLICT;
import static com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory.EXTERNAL_ERROR;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.constants.Constants.BD_COMPONENT_NAME;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.constants.Constants.CURRENT_COMPONENT_NAME;
import static org.apache.logging.log4j.util.Strings.EMPTY;

/**
 * Builder component that contains methods to build exception object.
 *
 * @author VI
 */
@Getter
@AllArgsConstructor
public enum CustomApiException {

  MB5001(EXTERNAL_ERROR,
      "MB5001", BD_COMPONENT_NAME, "Error de base de datos. "),
  MB4091(CONFLICT,
      "MB4091", CURRENT_COMPONENT_NAME, "No se encontró ningún producto para el ID proporcionado.");

  private final ErrorCategory category;

  private final String code;
  private final String componentName;
  private final String description;

  /**
   * Build and return a custom exception with description.
   *
   * @param throwable throwable
   * @return ApiException
   */
  public ApiException getException(final Throwable throwable) {
    return ApiException
        .builder()
        .category(this.getCategory())
        .systemCode(this.getCategory().getCode())
        .description(this.getCategory().getDescription())
        .errorType(this.getCategory().getErrorType())
        .addDetail(true)
        .withCode(this.getCode())
        .withComponent(this.getComponentName())
        .withDescription(this.getDescription().concat(throwable.getClass().getName())
            .concat(Optional.ofNullable(throwable.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(throwable.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY)))
        .push()
        .build();
  }

  /**
   * Build and return a custom exception with description.
   *
   * @param throwable throwable
   * @return ApiException
   */
  public ApiException getException(final String throwable) {
    return ApiException
        .builder()
        .category(this.getCategory())
        .systemCode(this.getCategory().getCode())
        .description(this.getCategory().getDescription())
        .errorType(this.getCategory().getErrorType())
        .addDetail(true)
        .withCode(this.getCode())
        .withComponent(this.getComponentName())
        .withDescription(this.getDescription().concat(throwable))
        .push()
        .build();
  }

  /**
   * Return exception.
   *
   * @return ApiException
   */
  public ApiException getException() {
    return ApiException
        .builder()
        .category(this.getCategory())
        .systemCode(this.getCategory().getCode())
        .description(this.getCategory().getDescription())
        .errorType(this.getCategory().getErrorType())
        .addDetail(true)
        .withCode(this.getCode())
        .withComponent(this.getComponentName())
        .withDescription(this.getDescription())
        .push()
        .build();
  }

}