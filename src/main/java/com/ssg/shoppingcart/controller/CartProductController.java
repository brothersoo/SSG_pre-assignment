package com.ssg.shoppingcart.controller;

import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductAddRequest;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductQuantityModifyRequest;
import com.ssg.shoppingcart.service.auth.AuthService;
import com.ssg.shoppingcart.service.cartproduct.CartProductService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart_product")
@RequiredArgsConstructor
public class CartProductController {

  private final CartProductService cartProductService;
  private final AuthService authService;

  @PostMapping("/{productId}")
  public ResponseEntity<CartProductInfo> cartProductAdd(
      @PathVariable("productId") Long productId,
      HttpServletRequest request,
      @RequestBody CartProductAddRequest requestBody
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.addProductToCart(
            user,
            productId,
            requestBody.getQuantity()
        ),
        HttpStatus.CREATED
    );
  }

  @GetMapping
  public ResponseEntity<Map<String, List<CartProductInfo>>> cartProductRetrieve(
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.findAllCartProductsForUser(user), HttpStatus.OK);
  }

  @PutMapping("/{cartProductId}")
  public ResponseEntity<CartProductInfo> cartProductQuantityModify(
      @PathVariable("cartProductId") Long cartProductId,
      HttpServletRequest request,
      @RequestBody CartProductQuantityModifyRequest requestBody
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.modifyCartProductQuantity(
            cartProductId, user, requestBody.getQuantity()
        ),
        HttpStatus.OK
    );
  }

  @DeleteMapping("/{cartProductId}")
  public ResponseEntity<Long> cartProductDelete(
      @PathVariable("cartProductId") Long cartProductId,
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.deleteCartProductById(cartProductId, user),
        HttpStatus.OK
    );
  }
}
