package com.ssg.shoppingcart.service.order;

import com.ssg.shoppingcart.dto.OrderDto.OrderInfo;
import java.util.List;

public interface OrderService {

  OrderInfo order(List<Long> cartProductIds, String userEmail);

  OrderInfo refund(Long orderId, String userEmail);
}
