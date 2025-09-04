package com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.web;


import com.lapositiva.services.productdirectory.products.infrastructure.inbound.controllers.utils.constants.ErrorCategory;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import static org.springframework.util.ClassUtils.isAssignable;

/**
 * Dummy <br/>
 * <b>Class</b>: {@link ErrorCategoryFromThrowable}<br/>
 * <p>
 * This class is used to map an exception to an error category.
 * </p>
 * <p>
 * The error categories are:
 * <ul>
 * <li>HOST_NOT_FOUND</li>
 * <li>EXTERNAL_TIMEOUT</li>
 * <li>SERVICE_UNAVAILABLE</li>
 * <li>UNEXPECTED</li>
 * </ul>
 * </p>
 * <p>
 * The exceptions are mapped to the error categories as follows:
 * <ul>
 * <li>UnknownHostException, NoRouteToHostException, MalformedURLException, URISyntaxException -> HOST_NOT_FOUND</li>
 * <li>SocketTimeoutException, SocketException, TimeoutException -> EXTERNAL_TIMEOUT</li>
 * <li>ConnectException -> SERVICE_UNAVAILABLE</li>
 * <li>Any other exception -> UNEXPECTED</li>
 * </ul>
 * </p>
 * <p>
 * The class is final and has a private constructor to prevent instantiation.
 * </p>
 */
public final class ErrorCategoryFromThrowable {

  private ErrorCategoryFromThrowable() {
  }

  /**
   * Maps an exception to an error category.
   *
   * @param ex the exception to map.
   * @return the error category.
   */
  public static ErrorCategory mapExceptionToCategory(final Exception ex) {
    Class<? extends Exception> exClass = ex.getClass();
    if (isAssignable(exClass, UnknownHostException.class)
        || isAssignable(exClass, NoRouteToHostException.class)
        || isAssignable(exClass, MalformedURLException.class)
        || isAssignable(exClass, URISyntaxException.class)) {
      return ErrorCategory.HOST_NOT_FOUND;
    } else if (isAssignable(exClass, SocketTimeoutException.class)
        || isAssignable(exClass, SocketException.class)
        || isAssignable(exClass, TimeoutException.class)) {
      return ErrorCategory.EXTERNAL_TIMEOUT;
    } else if (isAssignable(exClass, ConnectException.class)) {
      return ErrorCategory.SERVICE_UNAVAILABLE;
    }
    return ErrorCategory.UNEXPECTED;
  }
}
