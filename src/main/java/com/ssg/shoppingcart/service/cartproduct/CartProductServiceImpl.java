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
  public CartProductInfo addProductToCart(String userEmail, Long productId,
      int quantity, boolean addingIsConfirmed) {
    User user = userRepository.findByEmail(userEmail);
    if (user == null) {
      throw new IllegalArgumentException("no such user found with the given email");
    }

    Optional<Product> optionalProduct = productRepository.findById(productId);
    if (!optionalProduct.isPresent()) {
      throw new IllegalArgumentException("no such product with the given id");
    }
    Product product = optionalProduct.get();

    if (quantity <= 0 || quantity > product.getStock()) {
      throw new IllegalArgumentException("invalid quantity");
    }

    CartProduct cartProduct = cartProductRepository.findByUserAndProduct(user.getId(), productId);
    if (cartProduct != null) {
      if (addingIsConfirmed) {
        int newQuantity = cartProduct.getQuantity() + quantity;
        if (newQuantity <= 0 || newQuantity > product.getStock()) {
          throw new IllegalArgumentException("invalid quantity");
        } else {
          cartProduct.modifyQuantity(newQuantity);
        }
      } else {
        throw new IllegalArgumentException("adding quantity denied");
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
      throw new IllegalArgumentException("no such user found with the given email");
    }

    List<CartProductInfo> cartProductInfos = cartProductRepository.findAllByUserEmail(userEmail);
    Map<String, List<CartProductInfo>> groupedCartProductInfo = new HashMap<>();
    for (CartProductInfo cartProductInfo : cartProductInfos) {
      groupedCartProductInfo.putIfAbsent(
          cartProductInfo.getProduct().getProductGroupName(), new ArrayList<>());
      groupedCartProductInfo.compute(
          cartProductInfo.getProduct().getProductGroupName(),
          (String k, List<CartProductInfo> v) -> {
            v.add(cartProductInfo);
            return v;
          });
    }

    return groupedCartProductInfo;
  }

  @Override
  @Transactional
  public CartProductInfo modifyCartProductQuantity(Long cartProductId, int quantity) {
    Optional<CartProduct> optionalCartProduct = cartProductRepository.findById(cartProductId);
    if (!optionalCartProduct.isPresent()) {
      throw new IllegalArgumentException("no such cart product found with the given id");
    }

    CartProduct cartProduct = optionalCartProduct.get();

    if (quantity <= 0 || quantity > cartProduct.getProduct().getStock()) {
      throw new IllegalArgumentException("invalid quantity");
    }

    cartProduct.modifyQuantity(quantity);
    return modelMapper.map(cartProduct, CartProductInfo.class);
  }
}
