package io.brothersoo.ecommerce.service.cart_product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductInfo;
import io.brothersoo.ecommerce.dto.ProductDto.ProductInfo;
import io.brothersoo.ecommerce.dto.ProductGroupDto.ProductGroupInfo;
import io.brothersoo.ecommerce.repository.cartproduct.CartProductRepository;
import io.brothersoo.ecommerce.service.cartproduct.CartProductServiceImpl;
import io.brothersoo.ecommerce.validator.CartProductValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartProductListTest {

  @Mock
  CartProductRepository cartProductRepository;
  @InjectMocks
  CartProductServiceImpl cartProductService;
  @Spy
  CartProductValidator cartProductValidator;

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
      product.setStock(100);
      CartProductInfo cartProductInfo = new CartProductInfo();
      cartProductInfo.setProduct(product);
      cartProductInfo.setQuantity(i + 1);
      쓱배송상품리스트.add(cartProductInfo);
    }

    ProductGroupInfo 새벽배송 = new ProductGroupInfo();
    새벽배송.setName("새벽배송");
    for (int i = 0; i < 5; i++) {
      ProductInfo product = new ProductInfo();
      product.setProductGroup(새벽배송);
      product.setStock(100);
      CartProductInfo cartProductInfo = new CartProductInfo();
      cartProductInfo.setProduct(product);
      cartProductInfo.setQuantity(i + 1);
      새벽배송상품리스트.add(cartProductInfo);
    }

    ProductGroupInfo 택배 = new ProductGroupInfo();
    택배.setName("택배");
    for (int i = 0; i < 2; i++) {
      ProductInfo product = new ProductInfo();
      product.setProductGroup(택배);
      product.setStock(100);
      CartProductInfo cartProductInfo = new CartProductInfo();
      cartProductInfo.setProduct(product);
      cartProductInfo.setQuantity(i + 1);
      택배상품리스트.add(cartProductInfo);
    }

    List<CartProductInfo> allCartProducts = new ArrayList<>();
    allCartProducts.addAll(쓱배송상품리스트);
    allCartProducts.addAll(새벽배송상품리스트);
    allCartProducts.addAll(택배상품리스트);

    when(cartProductRepository.findAllByUserEmail(userEmail)).thenReturn(allCartProducts);

    Map<String, List<CartProductInfo>> groupedCartProductInfos
        = cartProductService.findAllCartProductsForUser(user);

    assertThat(groupedCartProductInfos.size()).isEqualTo(3);
    assertThat(groupedCartProductInfos.get("쓱배송")).isEqualTo(쓱배송상품리스트);
    assertThat(groupedCartProductInfos.get("새벽배송")).isEqualTo(새벽배송상품리스트);
    assertThat(groupedCartProductInfos.get("택배")).isEqualTo(택배상품리스트);

    verify(cartProductRepository, times(1)).findAllByUserEmail(anyString());
  }
}
