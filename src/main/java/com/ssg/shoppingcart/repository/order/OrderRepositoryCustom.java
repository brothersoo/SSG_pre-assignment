package com.ssg.shoppingcart.repository.order;

import com.ssg.shoppingcart.domain.order.Order;
import java.util.List;

public interface OrderRepositoryCustom {

  List<Order> findAllOrdersByUserEmail(String userEmail);
}
