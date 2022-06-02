package com.ssg.shoppingcart.repository.cartproduct;

import static com.ssg.shoppingcart.domain.QCartProduct.cartProduct;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.shoppingcart.domain.CartProduct;
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
}
