package com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.exception;

import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory;
import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.exception.ApiException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

import static com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory.CONFLICT;
import static com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory.EXTERNAL_ERROR;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.constants.Constants.BD_COMPONENT_NAME;
import static com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.constants.Constants.CURRENT_COMPONENT_NAME;
import static org.apache.logging.log4j.util.Strings.EMPTY;


/**
 * Builder component that contains methods to build exception object.
 *
 * @author vito.ivan
 */
@SuppressFBWarnings(value = "NM_CLASS_NOT_EXCEPTION", justification = "This class is derived from another exception")
@Getter
@AllArgsConstructor
public enum CustomApiException {

  C5001(EXTERNAL_ERROR,
      "C5001", BD_COMPONENT_NAME, "Error de base de datos. "),

  C4091(CONFLICT,
      "C4091", CURRENT_COMPONENT_NAME, "No se encontró ningún producto para el ID proporcionado.");

  private final ErrorCategory category;

  private final String code;
  private final String componentName;
  private final String description;

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

}
