package io.brothersoo.ecommerce.service.cart_product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.brothersoo.ecommerce.domain.product.CartProduct;
import io.brothersoo.ecommerce.domain.product.Product;
import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductInfo;
import io.brothersoo.ecommerce.repository.cartproduct.CartProductRepository;
import io.brothersoo.ecommerce.repository.user.UserRepository;
import io.brothersoo.ecommerce.service.cartproduct.CartProductServiceImpl;
import io.brothersoo.ecommerce.validator.CartProductValidator;
import io.brothersoo.ecommerce.validator.ProductValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class CartProductModifyTest {

  @Mock
  CartProductRepository cartProductRepository;
  @Mock
  UserRepository userRepository;
  @InjectMocks
  CartProductServiceImpl cartProductService;
  @Spy
  CartProductValidator cartProductValidator;
  @Spy
  ProductValidator productValidator;
  @Spy
  ModelMapper modelMapper = new ModelMapper();

  @Test
  @DisplayName("장바구니 상품 수량 변경 테스트")
  void cartProductQuantityTest() {
    Product product = Product.builder().stock(10).build();
    final String userEmail = "a@a.com";
    User user = User.builder().email(userEmail).build();
    final Long cartProductId = 1L;
    CartProduct cartProduct = CartProduct.builder().user(user).product(product).quantity(1).build();

    when(cartProductRepository.findByIdFetchProduct(cartProductId)).thenReturn(cartProduct);

    final int quantity = product.getStock() - 1;

    CartProductInfo cartProductInfo = cartProductService.modifyCartProductQuantity(
        cartProductId, user, quantity);

    assertThat(cartProductInfo.getQuantity()).isEqualTo(quantity);

    verify(cartProductRepository, times(1)).findByIdFetchProduct(anyLong());
  }

  @Test
  @DisplayName("변경 수량 재고 초과 테스트")
  void quantityExceedStockTest() {
    Product product = Product.builder().stock(10).build();
    final String userEmail = "a@a.com";
    User user = User.builder().email(userEmail).build();
    final Long cartProductId = 1L;
    CartProduct cartProduct = CartProduct.builder().user(user).product(product).quantity(1).build();
    when(cartProductRepository.findByIdFetchProduct(cartProductId)).thenReturn(cartProduct);

    final int quantity = product.getStock() + 1;

    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.modifyCartProductQuantity(cartProductId, user, quantity));

    verify(cartProductRepository, times(1)).findByIdFetchProduct(anyLong());
  }

  @Test
  @DisplayName("변경 수량 재고 0 이하 테스트")
  void quantityUnderZeroStockTest() {
    Product product = Product.builder().stock(10).build();
    final String userEmail = "a@a.com";
    User user = User.builder().email(userEmail).build();
    final Long cartProductId = 1L;
    CartProduct cartProduct = CartProduct.builder().user(user).product(product).quantity(1).build();
    when(cartProductRepository.findByIdFetchProduct(cartProductId)).thenReturn(cartProduct);

    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.modifyCartProductQuantity(cartProductId, user, 0));
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.modifyCartProductQuantity(cartProductId, user, -1));

    verify(cartProductRepository, times(2)).findByIdFetchProduct(anyLong());
  }

  @Test
  @DisplayName("장바구니 물품 확인 불가 테스트")
  void invalidUser() {
    Product product = Product.builder().stock(10).build();
    final Long cartProductId = 1L;
    final String userEmail = "a@a.com";
    User user = User.builder().email(userEmail).build();
    when(cartProductRepository.findByIdFetchProduct(cartProductId)).thenReturn(null);

    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.modifyCartProductQuantity(cartProductId, user, 1));

    verify(cartProductRepository, times(1)).findByIdFetchProduct(anyLong());
  }
}
