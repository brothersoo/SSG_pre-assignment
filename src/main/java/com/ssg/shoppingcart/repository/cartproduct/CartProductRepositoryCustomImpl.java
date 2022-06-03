package com.ssg.shoppingcart.repository.cartproduct;

import static com.ssg.shoppingcart.domain.QCartProduct.cartProduct;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.shoppingcart.domain.CartProduct;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartProductRepositoryCustomImpl implements CartProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public CartProduct findByUserAndProduct(Long userId, Long productId) {
    return queryFactory.selectFrom(cartProduct)
        .join(cartProduct.user).fetchJoin()
        .join(cartProduct.product).fetchJoin()
        .where(cartProduct.user.id.eq(userId), cartProduct.product.id.eq(productId))
        .fetchOne();
  }

  @Override
  public List<CartProductInfo> findAllByUserEmail(String email) {
    return queryFactory
        .select(Projections.constructor(CartProductInfo.class,
            cartProduct.id, cartProduct.quantity,
            cartProduct.product.id, cartProduct.product.name,
            cartProduct.product.price, cartProduct.product.stock,
            cartProduct.product.productGroup.name))
        .from(cartProduct)
        .join(cartProduct.user)
        .join(cartProduct.product)
        .join(cartProduct.product.productGroup)
        .where(cartProduct.user.email.eq(email))
        .fetch();
  }
}
