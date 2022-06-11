package com.ssg.shoppingcart.validator;

import com.ssg.shoppingcart.domain.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

  public void validateOrderableQuantity(Product product, int quantity) {
    if (quantity <= 0 || quantity > product.getStock()) {
      throw new IllegalArgumentException("invalid quantity value " + quantity);
    }
  }
}
