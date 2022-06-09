package com.ssg.shoppingcart.service.cartproduct;

import com.ssg.shoppingcart.domain.CartProduct;
import com.ssg.shoppingcart.domain.Product;
import com.ssg.shoppingcart.domain.User;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import com.ssg.shoppingcart.repository.cartproduct.CartProductRepository;
import com.ssg.shoppingcart.repository.product.ProductRepository;
import com.ssg.shoppingcart.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartProductServiceImpl implements CartProductService {

  private final CartProductRepository cartProductRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;

  @Override
  @Transactional
  public CartProductInfo addProductToCart(String userEmail, Long productId, int quantity) {
    User user = userRepository.findByEmail(userEmail);
    if (user == null) {
      throw new IllegalArgumentException("no such user found");
    }

    Optional<Product> optionalProduct = productRepository.findById(productId);
    if (!optionalProduct.isPresent()) {
      throw new IllegalArgumentException("no such product with the given id");
    }
    Product product = optionalProduct.get();

    if (quantity <= 0 || quantity > product.getStock()) {
      throw new IllegalArgumentException("invalid quantity value " + quantity);
    }

    CartProduct cartProduct = cartProductRepository.findByUserAndProduct(user.getId(), productId);
    if (cartProduct != null) {
      int newQuantity = cartProduct.getQuantity() + quantity;
      if (newQuantity <= 0 || newQuantity > product.getStock()) {
        throw new IllegalArgumentException("invalid total quantity " + newQuantity);
      } else {
        cartProduct.modifyQuantity(newQuantity);
      }
    } else {
      cartProduct = CartProduct.builder()
          .user(user).product(product).quantity(quantity).build();
      user.getCartProducts().add(cartProduct);
    }

    return modelMapper.map(cartProductRepository.save(cartProduct), CartProductInfo.class);
  }

  @Override
  public Map<String, List<CartProductInfo>> findAllCartProductsForUser(String userEmail) {
    User user = userRepository.findByEmail(userEmail);
    if (user == null) {
      throw new IllegalArgumentException("invalid user");
    }

    List<CartProductInfo> cartProductInfos = cartProductRepository.findAllByUserEmail(userEmail);
    Map<String, List<CartProductInfo>> groupedCartProductInfo = new HashMap<>();
    for (CartProductInfo cartProductInfo : cartProductInfos) {
      groupedCartProductInfo.putIfAbsent(
          cartProductInfo.getProduct().getProductGroup().getName(), new ArrayList<>());
      groupedCartProductInfo.compute(
          cartProductInfo.getProduct().getProductGroup().getName(),
          (String k, List<CartProductInfo> v) -> {
            v.add(cartProductInfo);
            return v;
          });
    }

    return groupedCartProductInfo;
  }

  @Override
  @Transactional
  public CartProductInfo modifyCartProductQuantity(
      Long cartProductId, String userEmail, int quantity
  ) {
    Optional<CartProduct> optionalCartProduct = cartProductRepository.findById(cartProductId);
    if (!optionalCartProduct.isPresent()) {
      throw new IllegalArgumentException("no such cart product found with the given id");
    }
    CartProduct cartProduct = optionalCartProduct.get();

    if (!cartProduct.getUser().getEmail().equals(userEmail)) {
      throw new IllegalArgumentException("not the owner of the cart product");
    }

    if (quantity <= 0 || quantity > cartProduct.getProduct().getStock()) {
      throw new IllegalArgumentException("invalid quantity");
    }

    cartProduct.modifyQuantity(quantity);
    return modelMapper.map(cartProduct, CartProductInfo.class);
  }

  @Override
  public Long deleteCartProductById(Long cartProductId, String userEmail) {
    Optional<CartProduct> optionalCartProduct = cartProductRepository.findById(cartProductId);
    if (!optionalCartProduct.isPresent()) {
      throw new IllegalArgumentException("no such cart product");
    }
    CartProduct cartProduct = optionalCartProduct.get();
    if (!cartProduct.getUser().getEmail().equals(userEmail)) {
      throw new IllegalArgumentException("not the owner of the cart product");
    }
    cartProductRepository.delete(cartProduct);
    return cartProductId;
  }
}
