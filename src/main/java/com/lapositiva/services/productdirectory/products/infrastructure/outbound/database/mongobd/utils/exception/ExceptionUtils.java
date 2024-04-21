package com.lapositiva.services.productdirectory.products.infrastructure.outbound.database.mongobd.utils.exception;

import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.exception.ApiException;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.MongoWriteException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;

import java.util.Map;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.EMPTY;

/**
 * Utility class for handling exceptions.
 */
public final class ExceptionUtils {
  private ExceptionUtils() {
  }

  /**
   * Build an API exception from a MongoDB throwable.
   *
   * @param customApiException the custom API exception.
   * @param ex                 the throwable.
   * @return the API exception.
   */
  public static ApiException buildApiExceptionFromMongoDbThrowable(
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
      Map.of(MongoTimeoutException.class, new MongoTimeoutExceptionHandler(),
          MongoSocketException.class, new MongoSocketExceptionHandler(),
          MongoCommandException.class, new MongoCommandExceptionHandler(),
          MongoWriteException.class, new MongoWriteExceptionHandler(),
          UncategorizedMongoDbException.class, new UncategorizedMongoDbExceptionHandler());

  public interface DbExceptionHandler {
    String description(Throwable throwable);
  }

  private static class UncategorizedMongoDbExceptionHandler implements DbExceptionHandler {
    @Override
    public String description(final Throwable ex) {
      if (ex instanceof UncategorizedMongoDbException exception) {
        return exception.getClass().getName()
            .concat(Optional.ofNullable(exception.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(exception.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY));
      }
      return EMPTY;
    }
  }

  private static class MongoTimeoutExceptionHandler implements DbExceptionHandler {
    @Override
    public String description(final Throwable ex) {
      if (ex instanceof MongoTimeoutException exception) {
        return exception.getClass().getName()
            .concat(Optional.ofNullable(exception.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(exception.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY));
      }
      return EMPTY;
    }
  }

  private static class MongoSocketExceptionHandler implements DbExceptionHandler {
    @Override
    public String description(final Throwable ex) {
      if (ex instanceof MongoSocketException exception) {
        return exception.getClass().getName()
            .concat(Optional.ofNullable(exception.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(exception.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY));
      }
      return EMPTY;
    }
  }

  private static class MongoCommandExceptionHandler implements DbExceptionHandler {
    @Override
    public String description(final Throwable ex) {
      if (ex instanceof MongoCommandException exception) {
        return exception.getClass().getName()
            .concat(Optional.ofNullable(exception.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(exception.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY));
      }
      return EMPTY;
    }
  }

  private static class MongoWriteExceptionHandler implements DbExceptionHandler {
    @Override
    public String description(final Throwable ex) {
      if (ex instanceof MongoWriteException exception) {
        return exception.getClass().getName()
            .concat(Optional.ofNullable(exception.getMessage()).map(" : "::concat).orElse(EMPTY))
            .concat(Optional.ofNullable(exception.getCause()).map(th -> ". ".concat(th.getMessage())).orElse(EMPTY));
      }
      return EMPTY;
    }
  }

}