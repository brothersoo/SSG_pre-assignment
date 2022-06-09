package com.ssg.shoppingcart.service.cartproduct;

import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import java.util.List;
import java.util.Map;

public interface CartProductService {

  CartProductInfo addProductToCart(String userEmail, Long productId, int quantity);

  Map<String, List<CartProductInfo>> findAllCartProductsForUser(String userEmail);

  CartProductInfo modifyCartProductQuantity(Long cartProductId, String userEmail, int quantity);

  Long deleteCartProductById(Long cartProductId, String UserEmail);
}
