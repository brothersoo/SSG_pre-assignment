package com.ssg.shoppingcart.controller;

import com.ssg.shoppingcart.dto.OrderDto.OrderCartProducts;
import com.ssg.shoppingcart.dto.OrderDto.OrderInfo;
import com.ssg.shoppingcart.service.order.OrderService;
import com.ssg.shoppingcart.util.AuthUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  private final AuthUtil authUtil;

  @PostMapping
  public ResponseEntity<OrderInfo> order(
      HttpServletRequest request,
      @RequestBody OrderCartProducts orderCartProducts
  ) {
    String token = authUtil.isBearer(request);
    String userEmail = authUtil.decodeJWT(token).getSubject();

    OrderInfo order = orderService.order(orderCartProducts.getCartProductIds(), userEmail);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

  @PutMapping("{orderId}")
  public ResponseEntity<OrderInfo> refund(
      @PathVariable("orderId") Long orderId,
      HttpServletRequest request
  ) {
    String token = authUtil.isBearer(request);
    String userEmail = authUtil.decodeJWT(token).getSubject();

    OrderInfo order = orderService.refund(orderId, userEmail);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }
}
