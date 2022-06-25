package io.brothersoo.ecommerce.service.cart_product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.brothersoo.ecommerce.domain.product.CartProduct;
import io.brothersoo.ecommerce.domain.product.Product;
import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductInfo;
import io.brothersoo.ecommerce.repository.cartproduct.CartProductRepository;
import io.brothersoo.ecommerce.service.cartproduct.CartProductServiceImpl;
import io.brothersoo.ecommerce.service.product.ProductService;
import io.brothersoo.ecommerce.validator.CartProductValidator;
import io.brothersoo.ecommerce.validator.ProductValidator;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class AddProductInCartServiceTest {

  @Mock
  CartProductRepository cartProductRepository;
  @Mock
  ProductService productService;
  @InjectMocks
  CartProductServiceImpl cartProductService;
  @Spy
  CartProductValidator cartProductValidator;
  @Spy
  ProductValidator productValidator;
  @Spy
  private ModelMapper modelMapper = new ModelMapper();

  @Test
  @DisplayName("카트에 이미 존재하는 상품 추가 성공 테스트")
  void addExistingProductToCartSuccessTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).stock(10).build();
    final int initialQuantity = 5;
    CartProduct cartProduct = CartProduct.builder().user(user).product(product)
        .quantity(initialQuantity).build();
    when(cartProductRepository.findByUserAndProduct(user.getId(), productId))
        .thenReturn(cartProduct);
    Mockito
        .when(cartProductRepository.save(any(CartProduct.class)))
        .thenAnswer(new Answer<CartProduct>() {
          @Override
          public CartProduct answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return (CartProduct) args[0];
          }
        });

    // when
    final int additionalQuantity = product.getStock() - initialQuantity - 1;
    CartProductInfo cartProductInfo
        = cartProductService.addProductToCart(user, productId, additionalQuantity);

    // then
    assertThat(cartProductInfo.getQuantity()).isEqualTo(initialQuantity + additionalQuantity);
    assertThat(cartProductInfo.getProduct().getId()).isEqualTo(productId);

    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(productService, times(0)).findByIdAndValidate(anyLong());
    verify(cartProductRepository, times(1)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("기존 수량 + 추가 수량 재고 초과 등록 실패 테스트")
  void addExistingProductToCartFailTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).stock(10).build();
    CartProduct cartProduct = CartProduct.builder().user(user).product(product).quantity(5).build();
    when(cartProductRepository.findByUserAndProduct(user.getId(), productId))
        .thenReturn(cartProduct);

    // when
    final int quantity = product.getStock() - 1;

    // then
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.addProductToCart(user, productId, quantity));

    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(productService, times(0)).findByIdAndValidate(anyLong());
    verify(cartProductRepository, times(0)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("기존 수량 + 추가 수량 음수로 인한 장바구니 등록 실패 테스트")
  void negativeQuantityTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).stock(10).build();
    CartProduct cartProduct = CartProduct.builder().user(user).product(product).quantity(5).build();
    when(cartProductRepository.findByUserAndProduct(user.getId(), productId))
        .thenReturn(cartProduct);

    // when
    final int quantity = -cartProduct.getQuantity() - 1;

    // then
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.addProductToCart(user, productId, quantity));

    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(productService, times(0)).findByIdAndValidate(anyLong());
    verify(cartProductRepository, times(0)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("새로운 상품 장바구니 추가 성공 테스트")
  void addNewProductToCartTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).stock(10).build();
    when(productService.findByIdAndValidate(1L)).thenReturn(product);
    when(cartProductRepository.findByUserAndProduct(user.getId(), productId)).thenReturn(null);
    Mockito
        .when(cartProductRepository.save(any(CartProduct.class)))
        .thenAnswer(new Answer<CartProduct>() {
          @Override
          public CartProduct answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return (CartProduct) args[0];
          }
        });

    // when
    final int quantity = product.getStock() - 1;
    CartProductInfo cartProductInfo
        = cartProductService.addProductToCart(user, productId, quantity);

    // then
    assertThat(cartProductInfo.getQuantity()).isEqualTo(quantity);
    assertThat(cartProductInfo.getProduct().getId()).isEqualTo(productId);

    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(productService, times(1)).findByIdAndValidate(anyLong());
    verify(cartProductRepository, times(1)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("새로운 상품 장바구니 등록 시 재고 초과로 인한 실패 테스트")
  void stockExceedQuantityTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).stock(10).build();
    when(cartProductRepository.findByUserAndProduct(user.getId(), productId)).thenReturn(null);
    when(productService.findByIdAndValidate(1L)).thenReturn(product);

    // when
    final int quantity = product.getStock() + 1;

    // then
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.addProductToCart(user, productId, quantity));

    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(productService, times(1)).findByIdAndValidate(anyLong());
    verify(cartProductRepository, times(0)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("카트에 추가 시 상품 탐색 실패 테스트")
  void noProductFoundInAddingProductToCartTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    when(productService.findByIdAndValidate(anyLong())).thenThrow(IllegalArgumentException.class);

    // when
    final int quantity = 10;

    // then
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.addProductToCart(user, productId, quantity)
    );

    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(productService, times(1)).findByIdAndValidate(anyLong());
    verify(cartProductRepository, times(0)).save(any(CartProduct.class));
  }
}
