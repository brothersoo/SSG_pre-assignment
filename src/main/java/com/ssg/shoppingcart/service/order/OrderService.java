package com.ssg.shoppingcart.service.order;

import com.ssg.shoppingcart.domain.order.Order;
import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.dto.OrderDto.OrderInfo;
import java.util.List;

public interface OrderService {

  Order findByIdAndValidate(Long orderId);

  OrderInfo order(List<Long> cartProductIds, User user);

  OrderInfo refund(Long orderId, User user);

  List<OrderInfo> findAllOrdersForUser(User user);
}
