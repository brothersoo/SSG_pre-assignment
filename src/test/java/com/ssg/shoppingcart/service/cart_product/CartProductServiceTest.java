package com.ssg.shoppingcart.service.cart_product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ssg.shoppingcart.domain.CartProduct;
import com.ssg.shoppingcart.domain.Product;
import com.ssg.shoppingcart.domain.User;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import com.ssg.shoppingcart.repository.cartproduct.CartProductRepository;
import com.ssg.shoppingcart.repository.product.ProductRepository;
import com.ssg.shoppingcart.repository.user.UserRepository;
import com.ssg.shoppingcart.service.cartproduct.CartProductServiceImpl;
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
class CartProductServiceTest {

  @Mock
  CartProductRepository cartProductRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  ProductRepository productRepository;
  @InjectMocks
  CartProductServiceImpl cartProductService;
  @Spy
  private ModelMapper modelMapper = new ModelMapper();

  @Test
  @DisplayName("카트에 새로운 상품 추가 성공 테스트")
  void addNewProductToCartTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).build();
    when(userRepository.findByEmail(email)).thenReturn(user);
    when(productRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(product));
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
    final int quantity = 10;
    final boolean addingIsConfirmed = false;
    CartProductInfo cartProductInfo
        = cartProductService.addProductToCart(email, productId, quantity, addingIsConfirmed);

    // then
    assertThat(cartProductInfo.getQuantity()).isEqualTo(quantity);
    assertThat(cartProductInfo.getProduct().getId()).isEqualTo(productId);
    assertThat(cartProductInfo.getUser().getEmail()).isEqualTo(email);

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(productRepository, times(1)).findById(anyLong());
    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(cartProductRepository, times(1)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("카트에 이미 존재하는 상품 추가 성공 테스트")
  void addExistingProductToCartSuccessTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).build();
    final int initialQuantity = 5;
    CartProduct cartProduct = CartProduct.builder().user(user).product(product)
        .quantity(initialQuantity).build();
    when(userRepository.findByEmail(email)).thenReturn(user);
    when(productRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(product));
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
    final int newQuantity = 10;
    final boolean addingIsConfirmed = true;
    CartProductInfo cartProductInfo
        = cartProductService.addProductToCart(email, productId, newQuantity, addingIsConfirmed);

    // then
    assertThat(cartProductInfo.getQuantity()).isEqualTo(initialQuantity + newQuantity);
    assertThat(cartProductInfo.getProduct().getId()).isEqualTo(productId);
    assertThat(cartProductInfo.getUser().getEmail()).isEqualTo(email);

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(productRepository, times(1)).findById(anyLong());
    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(cartProductRepository, times(1)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("카트에 이미 존재하는 상품 추가 실패 테스트")
  void addExistingProductToCartFailTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    Product product = Product.builder().id(productId).build();
    CartProduct cartProduct = CartProduct.builder().user(user).product(product).quantity(5).build();
    when(userRepository.findByEmail(email)).thenReturn(user);
    when(productRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(product));
    when(cartProductRepository.findByUserAndProduct(user.getId(), productId))
        .thenReturn(cartProduct);

    // when
    final int quantity = 10;
    final boolean addingIsConfirmed = false;

    // then
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.addProductToCart(email, productId, quantity, addingIsConfirmed));

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(productRepository, times(1)).findById(anyLong());
    verify(cartProductRepository, times(1))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(cartProductRepository, times(0)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("카트에 추가 시 사용자 탐색 실패 테스트")
  void noUserFoundInAddingProductToCartTest() {
    // given
    final String email = "abc@cba.com";
    final Long productId = 1L;
    when(userRepository.findByEmail(email)).thenReturn(null);

    // when
    final int quantity = 10;
    final boolean addingIsConfirmed = false;

    // then
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.addProductToCart(email, productId, quantity, addingIsConfirmed));

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(productRepository, times(0)).findById(anyLong());
    verify(cartProductRepository, times(0))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(cartProductRepository, times(0)).save(any(CartProduct.class));
  }

  @Test
  @DisplayName("카트에 추가 시 상품 탐색 실패 테스트")
  void noProductFoundInAddingProductToCartTest() {
    // given
    final String email = "abc@cba.com";
    User user = User.builder().id(1L).email(email).cartProducts(new ArrayList<>()).build();
    final Long productId = 1L;
    when(userRepository.findByEmail(email)).thenReturn(user);
    when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());

    // when
    final int quantity = 10;
    final boolean addingIsConfirmed = false;

    // then
    Assertions.assertThrows(IllegalArgumentException.class, () ->
        cartProductService.addProductToCart(email, productId, quantity, addingIsConfirmed));

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(productRepository, times(1)).findById(anyLong());
    verify(cartProductRepository, times(0))
        .findByUserAndProduct(anyLong(), anyLong());
    verify(cartProductRepository, times(0)).save(any(CartProduct.class));
  }
}
