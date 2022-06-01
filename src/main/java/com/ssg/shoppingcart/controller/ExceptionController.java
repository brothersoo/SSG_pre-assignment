package com.ssg.shoppingcart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

  @ExceptionHandler({ Exception.class })
  public ResponseEntity<Object> handleAll(final Exception ex) {
    log.info(ex.getClass().getName());
    log.error("error", ex);
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
