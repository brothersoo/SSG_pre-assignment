package io.brothersoo.ecommerce.validator;

import io.brothersoo.ecommerce.domain.order.Order;
import io.brothersoo.ecommerce.domain.order.OrderStatus;
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
    if (order.getStatus() != OrderStatus.COMPLETED) {
      throw new IllegalStateException("not a refundable order");
    }
  }
}
