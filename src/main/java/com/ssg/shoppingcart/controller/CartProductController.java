package com.ssg.shoppingcart.controller;

import com.ssg.shoppingcart.dto.CartProductDto.CartProductAddRequest;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import com.ssg.shoppingcart.service.cartproduct.CartProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart_product")
@RequiredArgsConstructor
public class CartProductController {

  private final CartProductService cartProductService;

  @PostMapping("/{productId}")
  public ResponseEntity<CartProductInfo> CartProductAdd(
      @PathVariable("productId") Long productId,
      @RequestBody CartProductAddRequest requestBody) {
    return new ResponseEntity<>(cartProductService.addProductToCart(
        requestBody.getUserEmail(),
        productId,
        requestBody.getQuantity(),
        requestBody.getAddingIsConfirmed()
    ),
        HttpStatus.CREATED);
  }
}
