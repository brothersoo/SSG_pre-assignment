package com.ssg.shoppingcart.exception.order;

public class ProductOutOfStockException extends RuntimeException {

  public ProductOutOfStockException(String message) {
    super(message);
  }
}
