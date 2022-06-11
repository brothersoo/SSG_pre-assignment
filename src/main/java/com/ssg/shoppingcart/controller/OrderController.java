package com.ssg.shoppingcart.controller;

import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.dto.OrderDto.OrderCartProducts;
import com.ssg.shoppingcart.dto.OrderDto.OrderInfo;
import com.ssg.shoppingcart.service.auth.AuthService;
import com.ssg.shoppingcart.service.order.OrderService;
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

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final AuthService authService;

  @PostMapping
  public ResponseEntity<OrderInfo> order(
      HttpServletRequest request,
      @RequestBody OrderCartProducts orderCartProducts
  ) {
    User user = authService.findUserByHttpRequest(request);

    OrderInfo order = orderService.order(orderCartProducts.getCartProductIds(), user);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

  @PutMapping("/refund/{orderId}")
  public ResponseEntity<OrderInfo> refund(
      @PathVariable("orderId") Long orderId,
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    OrderInfo order = orderService.refund(orderId, user);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<OrderInfo>> orderRetrieve(
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    List<OrderInfo> orderInfos = orderService.findAllOrdersForUser(user);
    return new ResponseEntity<>(orderInfos, HttpStatus.OK);
  }
}
