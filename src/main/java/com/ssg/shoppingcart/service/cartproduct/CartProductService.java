package com.ssg.shoppingcart.service.cartproduct;

import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;

public interface CartProductService {

  CartProductInfo addProductToCart(
      String userEmail, Long productId, int quantity, boolean addingIsConfirmed);
}
