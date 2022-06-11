package com.ssg.shoppingcart.util;

import com.ssg.shoppingcart.exception.auth.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<Object> handleAuthException(AuthException ex) {
    log.info(ex.getClass().getName());
    log.error("error", ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAll(Exception ex) {
    log.info(ex.getClass().getName());
    log.error("error", ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
