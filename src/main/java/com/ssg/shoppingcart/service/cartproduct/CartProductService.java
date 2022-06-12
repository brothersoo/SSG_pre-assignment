package com.ssg.shoppingcart.service.cartproduct;

import com.ssg.shoppingcart.domain.product.CartProduct;
import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import java.util.List;
import java.util.Map;

public interface CartProductService {

  CartProduct findByIdAndValidate(Long cartProductId);

  CartProductInfo addProductToCart(User user, Long productId, int quantity);

  Map<String, List<CartProductInfo>> findAllCartProductsForUser(User user);

  CartProductInfo modifyCartProductQuantity(Long cartProductId, User user, int quantity);

  Long deleteCartProductById(Long cartProductId, User user);

  List<Long> handleCartProductQuantityExceededStock(String type, User user);

  void resetCartProductQuantityExceededStock(User user, List<Long> cartProductIds);

  void removeCartProductQuantityExceededStock(User user, List<Long> cartProductIds);
}
