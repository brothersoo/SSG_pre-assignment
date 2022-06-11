package com.ssg.shoppingcart.service.order;

import static com.ssg.shoppingcart.domain.OrderStatus.COMPLETED;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

  /***
   *
   * @param cartProductIds
   * @param userEmail
   * @return 완성된 주문의 정보
   *
   * 사용자 장바구니에서 선택된 상품들을 주문 처리하는 로직입니다.
   * 실제 로직에서는 상태가 DELIVERY로 저장되어야 하지만 간소화로 인해 바로 주문 완료인 COMPLETED로 저장됩니다.
   */
  @Override
  @Transactional
  public OrderInfo order(List<Long> cartProductIds, String userEmail) {
    List<CartProduct> cartProducts = cartProductRepository.findAllById(cartProductIds);
    Set<OrderProduct> orderProducts = new HashSet<>();
    User user = userRepository.findByEmail(userEmail);
    if (user == null) {
      throw new IllegalArgumentException("no user found");
    }
    Order order = Order.builder().status(COMPLETED).user(user).build();
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

  @Override
  public List<OrderInfo> findAllOrdersForUser(String userEmail) {
    List<Order> orders = orderRepository.findAllOrdersByUserEmail(userEmail);
    return orders.stream()
        .map(order -> modelMapper.map(order, OrderInfo.class))
        .collect(Collectors.toList());
  }
}
