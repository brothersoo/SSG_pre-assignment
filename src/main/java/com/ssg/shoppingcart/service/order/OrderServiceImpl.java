package com.ssg.shoppingcart.service.order;

import static com.ssg.shoppingcart.domain.OrderStatus.CONFIRMED;
import static com.ssg.shoppingcart.domain.OrderStatus.REFUND;

import com.ssg.shoppingcart.domain.CartProduct;
import com.ssg.shoppingcart.domain.Order;
import com.ssg.shoppingcart.domain.OrderProduct;
import com.ssg.shoppingcart.domain.User;
import com.ssg.shoppingcart.dto.OrderDto.OrderInfo;
import com.ssg.shoppingcart.repository.cartproduct.CartProductRepository;
import com.ssg.shoppingcart.repository.order.OrderRepository;
import com.ssg.shoppingcart.repository.orderproduct.OrderProductRepository;
import com.ssg.shoppingcart.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CartProductRepository cartProductRepository;
  private final OrderProductRepository orderProductRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  @Transactional
  public OrderInfo order(List<Long> cartProductIds, String userEmail) {
    List<CartProduct> cartProducts = cartProductRepository.findAllById(cartProductIds);
    List<OrderProduct> orderProducts = new ArrayList<>();
    User user = userRepository.findByEmail(userEmail);
    if (user == null) {
      throw new IllegalArgumentException("no user found");
    }
    Order order = Order.builder().status(CONFIRMED).user(user).build();
    Order persistedOrder = orderRepository.save(order);

    for (CartProduct cartProduct : cartProducts) {
      if (cartProduct.isOutOfStock()) {
        throw new RuntimeException("product out of stock");
      }
      if (!cartProduct.getUser().getEmail().equals(userEmail)) {
        throw new IllegalArgumentException("unauthorized");
      }

      cartProduct.orderQuantity();
      OrderProduct orderProduct = OrderProduct.builder()
          .price(cartProduct.getProduct().getPrice())
          .quantity(cartProduct.getQuantity())
          .order(persistedOrder)
          .product(cartProduct.getProduct())
          .build();
      orderProductRepository.save(orderProduct);
      orderProducts.add(orderProduct);
    }
    persistedOrder.setOrderProducts(orderProducts);
    cartProductRepository.deleteAllById(cartProductIds);
    return modelMapper.map(persistedOrder, OrderInfo.class);
  }

  @Override
  @Transactional
  public OrderInfo refund(Long orderId, String userEmail) {
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    if (!optionalOrder.isPresent()) {
      throw new IllegalArgumentException("order not found");
    }

    Order order = optionalOrder.get();
    if (!order.getUser().getEmail().equals(userEmail)) {
      throw new IllegalArgumentException("unauthorized");
    }
    order.changeStatus(REFUND);
    for (OrderProduct orderProduct : order.getOrderProducts()) {
      orderProduct.refundQuantity();
    }

    return modelMapper.map(order, OrderInfo.class);
  }
}
