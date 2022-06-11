package com.ssg.shoppingcart.validator;

import com.ssg.shoppingcart.domain.product.CartProduct;
import com.ssg.shoppingcart.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class CartProductValidator {

  public void validateOwner(CartProduct cartProduct, User user) {
    if (!cartProduct.getUser().getEmail().equals(user.getEmail())) {
      throw new IllegalArgumentException("not the owner of the cart product");
    }
  }

  public void validateIsInStock(CartProduct cartProduct) {
    if (cartProduct.isOutOfStock()) {
      throw new IllegalArgumentException("product out of stock");
    }
  }
}
