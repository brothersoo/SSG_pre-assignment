package com.ssg.shoppingcart.repository.cartproduct;

import static com.ssg.shoppingcart.domain.product.QCartProduct.cartProduct;
import static com.ssg.shoppingcart.domain.product.QProduct.product;
import static com.ssg.shoppingcart.domain.product.QProductGroup.productGroup;
import static com.ssg.shoppingcart.domain.user.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.shoppingcart.domain.product.CartProduct;
import com.ssg.shoppingcart.dto.CartProductDto.CartProductInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 장바구니 상품 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class CartProductRepositoryCustomImpl implements CartProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  /**
   * 장바구니 상품을 id로 검색
   * 연관된 상품을 fetch join
   */
  @Override
  public CartProduct findByIdFetchProduct(Long cartProductId) {
    return queryFactory.selectFrom(cartProduct)
        .join(cartProduct.product, product).fetchJoin()
        .join(product.productGroup, productGroup).fetchJoin()
        .where(cartProduct.id.eq(cartProductId))
        .fetchOne();
  }

  /**
   * 장바구니 상품을 id list로 검색
   * 연관된 상품을 fetch join
   */
  @Override
  public List<CartProduct> findAllByIdFetchProduct(List<Long> cartProductIds) {
    return queryFactory.selectFrom(cartProduct)
        .join(cartProduct.product, product).fetchJoin()
        .join(product.productGroup, productGroup).fetchJoin()
        .where(cartProduct.id.in(cartProductIds))
        .fetch();
  }

  /**
   * 유저의 장바구니 내 상품 중 주어진 상품 id에 해당하는 장바구니 상품 검색
   * 연관된 상품, 상품 그룹을 fetch join
   */
  @Override
  public CartProduct findByUserAndProduct(Long userId, Long productId) {
    return queryFactory.selectFrom(cartProduct)
        .join(cartProduct.user, user)
        .join(cartProduct.product, product).fetchJoin()
        .join(product.productGroup, productGroup).fetchJoin()
        .where(cartProduct.user.id.eq(userId), cartProduct.product.id.eq(productId))
        .fetchOne();
  }

  /**
   * 사용자의 장바구니 상품을 모두 검색
   */
  @Override
  public List<CartProductInfo> findAllByUserEmail(String email) {
    return queryFactory
        .select(Projections.constructor(CartProductInfo.class,
            cartProduct.id, cartProduct.quantity,
            cartProduct.product.id, cartProduct.product.name,
            cartProduct.product.price, cartProduct.product.stock,
            cartProduct.product.productGroup.id, cartProduct.product.productGroup.name))
        .from(cartProduct)
        .join(cartProduct.user)
        .join(cartProduct.product)
        .join(cartProduct.product.productGroup)
        .where(cartProduct.user.email.eq(email))
        .fetch();
  }

  @Override
  public Long deleteCartProductById(Long id) {
    queryFactory.delete(cartProduct).where(cartProduct.id.eq(id)).execute();
    return id;
  }

  @Override
  public List<CartProduct> findQuantityExceededStockFetchProduct(Long userId) {
    return queryFactory
        .selectFrom(cartProduct).distinct()
        .join(cartProduct.product, product)
        .fetchJoin()
        .join(cartProduct.user, user)
        .where(
            user.id.in(userId),
            cartProduct.quantity.gt(cartProduct.product.stock)
        )
        .fetch();
  }
}
