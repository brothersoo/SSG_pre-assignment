package io.brothersoo.ecommerce.repository.order;

import io.brothersoo.ecommerce.domain.order.Order;
import java.util.List;

public interface OrderRepositoryCustom {

  List<Order> findAllOrdersByUserEmail(String userEmail);
}
