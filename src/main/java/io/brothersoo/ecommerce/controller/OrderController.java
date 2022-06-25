package io.brothersoo.ecommerce.controller;

import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.OrderDto.OrderCartProducts;
import io.brothersoo.ecommerce.dto.OrderDto.OrderInfo;
import io.brothersoo.ecommerce.service.auth.AuthService;
import io.brothersoo.ecommerce.service.order.OrderService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문 관련 기련 controller
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final AuthService authService;

  /**
   * 선택한 장바구니 상품을 주문합니다.
   *
   * @param request           header에 담길 token을 위한 http request
   * @param orderCartProducts 주문할 장바구니 상품 id 리스트
   * @return 주문을 진행한 장바구니 상품 정보 리스트를 반환합니다.
   */
  @PostMapping
  public ResponseEntity<OrderInfo> order(
      HttpServletRequest request,
      @RequestBody OrderCartProducts orderCartProducts
  ) {
    User user = authService.findUserByHttpRequest(request);

    OrderInfo order = orderService.order(orderCartProducts.getCartProductIds(), user);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

  /**
   * 선택한 주문을 환불합니다.
   *
   * @param orderId 환불할 주문의 id
   * @param request header에 담길 token을 위한 http request
   * @return 환불 처리한 주문의 정보를 반환합니다.
   */
  @PutMapping("/refund/{orderId}")
  public ResponseEntity<OrderInfo> refund(
      @PathVariable("orderId") Long orderId,
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    OrderInfo order = orderService.refund(orderId, user);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }

  /**
   * 사용자의 주문 내역 리스트를 반환합니다.
   *
   * @param request header에 담길 token을 위한 http request
   * @return 사용자의 주문 내역 정보 리스트를 반환합니다.
   */
  @GetMapping
  public ResponseEntity<List<OrderInfo>> orderList(
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    List<OrderInfo> orderInfos = orderService.findAllOrdersForUser(user);
    return new ResponseEntity<>(orderInfos, HttpStatus.OK);
  }
}
