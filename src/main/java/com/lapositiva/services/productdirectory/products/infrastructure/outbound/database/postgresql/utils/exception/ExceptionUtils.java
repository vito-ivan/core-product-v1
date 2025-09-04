package com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.postgresql.utils.exception;

import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.exception.ApiException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.Map;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.EMPTY;

public final class ExceptionUtils {
  private ExceptionUtils() {
  }

  /**
   * Build an API exception from a PostgreSQL throwable.
   *
   * @param customApiException the custom API exception.
   * @param ex                 the throwable.
   * @return the API exception.
   */
  public static ApiException buildApiExceptionFromPostgresqlThrowable(
      final CustomApiException customApiException,
      final Throwable ex) {

    DbExceptionHandler handler = HANDLERS.get(ex.getClass());

    if (handler != null) {
      return customApiException.getException(handler.description(ex));
    }

    Throwable cause = ex.getCause();

    if (cause != null) {
      handler = HANDLERS.get(cause.getClass());

      if (handler != null) {
        return customApiException.getException(handler.description(cause));
      }
    }

    return customApiException.getException(ex);
  }

  private static final Map<Class<? extends Throwable>, DbExceptionHandler> HANDLERS =
      Map.of(DataAccessResourceFailureException.class, new DataAccessResourceFailureExceptionHandler(),
          BadSqlGrammarException.class, new BadSqlGrammarExceptionHandler());

  /**
   * Interface that contains methods to build custom exception object.
   *
   * @author VI
   */
  public interface DbExceptionHandler {

    String description(Throwable throwable);

  }

  private static class BadSqlGrammarExceptionHandler implements DbExceptionHandler {

    @Override
    public String description(final Throwable ex) {
      if (ex instanceof BadSqlGrammarException exception) {

        return exception.getClass().getName()
            .concat(Optional.ofNullable(exception.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(exception.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY));
      }
      return EMPTY;
    }

  }

  private static class DataAccessResourceFailureExceptionHandler implements DbExceptionHandler {

    @Override
    public String description(final Throwable ex) {
      if (ex instanceof DataAccessResourceFailureException exception) {
        return exception.getClass().getName()
            .concat(Optional.ofNullable(exception.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(exception.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY));
      }
      return EMPTY;
    }

  }

}