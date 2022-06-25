package io.brothersoo.ecommerce.service.order;

import static io.brothersoo.ecommerce.domain.order.OrderStatus.COMPLETED;
import static io.brothersoo.ecommerce.domain.order.OrderStatus.REFUND;

import io.brothersoo.ecommerce.domain.order.Order;
import io.brothersoo.ecommerce.domain.product.CartProduct;
import io.brothersoo.ecommerce.domain.product.OrderProduct;
import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.OrderDto.OrderInfo;
import io.brothersoo.ecommerce.repository.cartproduct.CartProductRepository;
import io.brothersoo.ecommerce.repository.order.OrderRepository;
import io.brothersoo.ecommerce.repository.orderproduct.OrderProductRepository;
import io.brothersoo.ecommerce.validator.CartProductValidator;
import io.brothersoo.ecommerce.validator.OrderValidator;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CartProductRepository cartProductRepository;
  private final OrderProductRepository orderProductRepository;
  private final CartProductValidator cartProductValidator;
  private final OrderValidator orderValidator;
  private final ModelMapper modelMapper;

  /**
   * id를 사용하여 주문을 검색합니다.<br/>
   * id에 해당하는 주문이 없을 시 에러를 발생합니다.
   */
  @Override
  public Order findByIdAndValidate(Long orderId) {
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    if (!optionalOrder.isPresent()) {
      throw new IllegalArgumentException("order not found");
    }
    return optionalOrder.get();
  }

  /***
   * 사용자 장바구니에서 선택된 상품들을 주문 처리하는 로직입니다.<br/>
   * 실제 로직에서는 상태가 DELIVERY로 저장되어야 하지만 간소화를 위해 바로 주문 완료인 COMPLETED로 저장됩니다.
   */
  @Override
  @Transactional
  public OrderInfo order(List<Long> cartProductIds, User user) {
    List<CartProduct> cartProducts = cartProductRepository.findAllByIdFetchProduct(cartProductIds);
    Set<OrderProduct> orderProducts = new HashSet<>();

    Order order = Order.builder().status(COMPLETED).user(user).build();
    Order persistedOrder = orderRepository.save(order);

    for (CartProduct cartProduct : cartProducts) {
      cartProductValidator.validateIsInStock(cartProduct);
      cartProductValidator.validateOwner(cartProduct, user);

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

  /**
   * 주문 상품을 환불합니다.<br/>
   * 주문의 상태가 REFUND_REQUESTED인 경우에만 처리하는 것이 올바르지만, 간소화를 위해 COMPLETED 상태의 상품을 환불합니다.<br/>
   * 주문 상태가 COMPLETED인지 검증합니다.
   */
  @Override
  @Transactional
  public OrderInfo refund(Long orderId, User user) {
    Order order = findByIdAndValidate(orderId);
    orderValidator.validateOrderRefundable(order);
    order.changeStatus(REFUND);
    for (OrderProduct orderProduct : order.getOrderProducts()) {
      orderProduct.refundQuantity();
    }
    return modelMapper.map(order, OrderInfo.class);
  }

  /**
   * 사용자의 모든 주문들을 반환합니다.
   */
  @Override
  public List<OrderInfo> findAllOrdersForUser(User user) {
    List<Order> orders = orderRepository.findAllOrdersByUserEmail(user.getEmail());
    return orders.stream()
        .map(order -> modelMapper.map(order, OrderInfo.class))
        .collect(Collectors.toList());
  }
}
