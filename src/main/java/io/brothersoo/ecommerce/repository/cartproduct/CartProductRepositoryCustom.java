package io.brothersoo.ecommerce.repository.cartproduct;

import io.brothersoo.ecommerce.domain.product.CartProduct;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductInfo;
import java.util.List;

public interface CartProductRepositoryCustom {

  CartProduct findByIdFetchProduct(Long productId);

  List<CartProduct> findAllByIdFetchProduct(List<Long> cartProductIds);

  CartProduct findByUserAndProduct(Long userId, Long productId);

  List<CartProductInfo> findAllByUserEmail(String email);

  Long deleteCartProductById(Long id);

  List<CartProduct> findQuantityExceededStockFetchProduct(Long userId);
}
