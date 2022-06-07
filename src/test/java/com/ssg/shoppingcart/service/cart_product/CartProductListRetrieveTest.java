package com.ssg.shoppingcart.service.cart_product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ssg.shoppingcart.domain.User;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import com.ssg.shoppingcart.dto.ProductGroupDto.ProductGroupInfo;
import com.ssg.shoppingcart.repository.cartproduct.CartProductRepository;
import com.ssg.shoppingcart.repository.user.UserRepository;
import com.ssg.shoppingcart.service.cartproduct.CartProductServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartProductListRetrieveTest {

  @Mock
  UserRepository userRepository;
  @Mock
  CartProductRepository cartProductRepository;
  @InjectMocks
  CartProductServiceImpl cartProductService;

  @Test
  @DisplayName("사용자 장바구니 리스트 테스트")
  void userCartListRetrieveTest() {
    final String userEmail = "abc@cba.com";
    User user = User.builder().email(userEmail).build();

    List<CartProductInfo> 쓱배송상품리스트 = new ArrayList<>();
    List<CartProductInfo> 새벽배송상품리스트 = new ArrayList<>();
    List<CartProductInfo> 택배상품리스트 = new ArrayList<>();

    ProductGroupInfo 쓱배송 = new ProductGroupInfo();
    쓱배송.setName("쓱배송");
    for (int i = 0; i < 3; i++) {
      ProductInfo product = new ProductInfo();
      product.setProductGroup(쓱배송);
      CartProductInfo cartProductInfo = new CartProductInfo();
      cartProductInfo.setProduct(product);
      쓱배송상품리스트.add(cartProductInfo);
    }

    ProductGroupInfo 새벽배송 = new ProductGroupInfo();
    새벽배송.setName("새벽배송");
    for (int i = 0; i < 5; i++) {
      ProductInfo product = new ProductInfo();
      product.setProductGroup(새벽배송);
      CartProductInfo cartProductInfo = new CartProductInfo();
      cartProductInfo.setProduct(product);
      새벽배송상품리스트.add(cartProductInfo);
    }

    ProductGroupInfo 택배 = new ProductGroupInfo();
    택배.setName("택배");
    for (int i = 0; i < 2; i++) {
      ProductInfo product = new ProductInfo();
      product.setProductGroup(택배);
      CartProductInfo cartProductInfo = new CartProductInfo();
      cartProductInfo.setProduct(product);
      택배상품리스트.add(cartProductInfo);
    }

    List<CartProductInfo> allCartProducts = new ArrayList<>();
    allCartProducts.addAll(쓱배송상품리스트);
    allCartProducts.addAll(새벽배송상품리스트);
    allCartProducts.addAll(택배상품리스트);

    when(userRepository.findByEmail(userEmail)).thenReturn(user);
    when(cartProductRepository.findAllByUserEmail(userEmail)).thenReturn(allCartProducts);

    Map<String, List<CartProductInfo>> groupedCartProductInfos
        = cartProductService.findAllCartProductsForUser(userEmail);

    assertThat(groupedCartProductInfos.size()).isEqualTo(3);
    assertThat(groupedCartProductInfos.get("쓱배송")).isEqualTo(쓱배송상품리스트);
    assertThat(groupedCartProductInfos.get("새벽배송")).isEqualTo(새벽배송상품리스트);
    assertThat(groupedCartProductInfos.get("택배")).isEqualTo(택배상품리스트);

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(cartProductRepository, times(1)).findAllByUserEmail(anyString());
  }

  @Test
  @DisplayName("알 수 없는 사용자 에러 테스트")
  void noSuchUserTest() {
    when(userRepository.findByEmail(anyString())).thenReturn(null);

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> cartProductService.findAllCartProductsForUser(anyString()));

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(cartProductRepository, times(0)).findAllByUserEmail(anyString());
  }
}
