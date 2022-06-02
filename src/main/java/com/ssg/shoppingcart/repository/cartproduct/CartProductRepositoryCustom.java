package com.ssg.shoppingcart.repository.cartproduct;

import com.ssg.shoppingcart.domain.CartProduct;

public interface CartProductRepositoryCustom {

  CartProduct findByUserAndProduct(Long userId, Long productId);
}
