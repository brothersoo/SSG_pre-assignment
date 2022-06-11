package com.ssg.shoppingcart.validator;

import static com.ssg.shoppingcart.domain.order.OrderStatus.COMPLETED;

import com.ssg.shoppingcart.domain.order.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

  public void validateOrderRefundable(Order order) {
    if (order.getStatus() != COMPLETED) {
      throw new IllegalStateException("not a refundable order");
    }
  }
}
