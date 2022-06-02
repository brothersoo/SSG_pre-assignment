package com.ssg.shoppingcart.controller;

import com.ssg.shoppingcart.dto.CartProductDto.CartProductAddRequest;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import com.ssg.shoppingcart.service.cartproduct.CartProductService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart_product")
@RequiredArgsConstructor
public class CartProductController {

  private final CartProductService cartProductService;

  // TODO: change getting user email from request body to JWT
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

  // TODO: change getting user email from param to JWT
  @GetMapping
  public ResponseEntity<Map<String, List<CartProductInfo>>> CartProductRetrieve(
      @RequestParam("userEmail") String userEmail) {
    return new ResponseEntity<>(
        cartProductService.findAllCartProductsForUser(userEmail), HttpStatus.OK);
  }
}
