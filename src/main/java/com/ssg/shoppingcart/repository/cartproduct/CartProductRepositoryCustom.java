package com.ssg.shoppingcart.repository.cartproduct;

import com.ssg.shoppingcart.domain.CartProduct;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import java.util.List;

public interface CartProductRepositoryCustom {

  CartProduct findByUserAndProduct(Long userId, Long productId);

  List<CartProductInfo> findAllByUserEmail(String email);
}
