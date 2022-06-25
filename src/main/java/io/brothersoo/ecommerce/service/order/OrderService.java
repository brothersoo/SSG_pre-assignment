package io.brothersoo.ecommerce.service.order;

import io.brothersoo.ecommerce.domain.order.Order;
import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.OrderDto.OrderInfo;
import java.util.List;

public interface OrderService {

  Order findByIdAndValidate(Long orderId);

  OrderInfo order(List<Long> cartProductIds, User user);

  OrderInfo refund(Long orderId, User user);

  List<OrderInfo> findAllOrdersForUser(User user);
}
