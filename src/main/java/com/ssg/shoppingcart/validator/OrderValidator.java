package com.ssg.shoppingcart.validator;

import static com.ssg.shoppingcart.domain.order.OrderStatus.COMPLETED;

import com.ssg.shoppingcart.domain.order.Order;
import org.springframework.stereotype.Component;

/**
 * 주문 서비스 내 검증
 */
@Component
public class OrderValidator {

  /**
   * 환불 가능한 주문 상태인지 검증
   */
  public void validateOrderRefundable(Order order) {
    if (order.getStatus() != COMPLETED) {
      throw new IllegalStateException("not a refundable order");
    }
  }
}
