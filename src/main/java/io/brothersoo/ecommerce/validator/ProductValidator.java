package io.brothersoo.ecommerce.validator;

import io.brothersoo.ecommerce.domain.product.Product;
import org.springframework.stereotype.Component;

/**
 * 상품 서비스 내 검증
 */
@Component
public class ProductValidator {

  /**
   * 지정한 수량이 상품의 범위 내인지 검증
   */
  public void validateOrderableQuantity(Product product, int quantity) {
    if (quantity <= 0 || quantity > product.getStock()) {
      throw new IllegalArgumentException("invalid quantity value " + quantity);
    }
  }
}
