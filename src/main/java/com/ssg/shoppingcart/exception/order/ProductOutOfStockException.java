package com.ssg.shoppingcart.exception.order;

/**
 * 장바구니 상품 수량이 상품 재고 범위에 맞지 않을 때 발생하는 예외
 */
public class ProductOutOfStockException extends RuntimeException {

  public ProductOutOfStockException(String message) {
    super(message);
  }
}
