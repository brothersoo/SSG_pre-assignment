package com.ssg.shoppingcart.repository.cartproduct;

import com.ssg.shoppingcart.domain.product.CartProduct;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import java.util.List;

public interface CartProductRepositoryCustom {

  CartProduct findByIdFetchProduct(Long productId);

  List<CartProduct> findAllByIdFetchProduct(List<Long> cartProductIds);

  CartProduct findByUserAndProduct(Long userId, Long productId);

  List<CartProductInfo> findAllByUserEmail(String email);

  Long deleteCartProductById(Long id);

  List<CartProduct> findQuantityExceededStockFetchProduct(Long userId);
}
