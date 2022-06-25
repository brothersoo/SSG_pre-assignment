package io.brothersoo.ecommerce.repository.cartproduct;

import static io.brothersoo.ecommerce.domain.user.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.brothersoo.ecommerce.domain.product.CartProduct;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductInfo;
import io.brothersoo.ecommerce.domain.product.QCartProduct;
import io.brothersoo.ecommerce.domain.product.QProduct;
import io.brothersoo.ecommerce.domain.product.QProductGroup;
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
    return queryFactory.selectFrom(QCartProduct.cartProduct)
        .join(QCartProduct.cartProduct.product, QProduct.product).fetchJoin()
        .join(QProduct.product.productGroup, QProductGroup.productGroup).fetchJoin()
        .where(QCartProduct.cartProduct.id.eq(cartProductId))
        .fetchOne();
  }

  /**
   * 장바구니 상품을 id list로 검색
   * 연관된 상품을 fetch join
   */
  @Override
  public List<CartProduct> findAllByIdFetchProduct(List<Long> cartProductIds) {
    return queryFactory.selectFrom(QCartProduct.cartProduct)
        .join(QCartProduct.cartProduct.product, QProduct.product).fetchJoin()
        .join(QProduct.product.productGroup, QProductGroup.productGroup).fetchJoin()
        .where(QCartProduct.cartProduct.id.in(cartProductIds))
        .fetch();
  }

  /**
   * 유저의 장바구니 내 상품 중 주어진 상품 id에 해당하는 장바구니 상품 검색
   * 연관된 상품, 상품 그룹을 fetch join
   */
  @Override
  public CartProduct findByUserAndProduct(Long userId, Long productId) {
    return queryFactory.selectFrom(QCartProduct.cartProduct)
        .join(QCartProduct.cartProduct.user, user)
        .join(QCartProduct.cartProduct.product, QProduct.product).fetchJoin()
        .join(QProduct.product.productGroup, QProductGroup.productGroup).fetchJoin()
        .where(QCartProduct.cartProduct.user.id.eq(userId),
            QCartProduct.cartProduct.product.id.eq(productId))
        .fetchOne();
  }

  /**
   * 사용자의 장바구니 상품을 모두 검색
   */
  @Override
  public List<CartProductInfo> findAllByUserEmail(String email) {
    return queryFactory
        .select(Projections.constructor(CartProductInfo.class,
            QCartProduct.cartProduct.id, QCartProduct.cartProduct.quantity,
            QCartProduct.cartProduct.product.id, QCartProduct.cartProduct.product.name,
            QCartProduct.cartProduct.product.price, QCartProduct.cartProduct.product.stock,
            QCartProduct.cartProduct.product.productGroup.id,
            QCartProduct.cartProduct.product.productGroup.name))
        .from(QCartProduct.cartProduct)
        .join(QCartProduct.cartProduct.user)
        .join(QCartProduct.cartProduct.product)
        .join(QCartProduct.cartProduct.product.productGroup)
        .where(QCartProduct.cartProduct.user.email.eq(email))
        .fetch();
  }

  @Override
  public Long deleteCartProductById(Long id) {
    queryFactory.delete(QCartProduct.cartProduct).where(QCartProduct.cartProduct.id.eq(id))
        .execute();
    return id;
  }

  @Override
  public List<CartProduct> findQuantityExceededStockFetchProduct(Long userId) {
    return queryFactory
        .selectFrom(QCartProduct.cartProduct).distinct()
        .join(QCartProduct.cartProduct.product, QProduct.product)
        .fetchJoin()
        .join(QCartProduct.cartProduct.user, user)
        .where(
            user.id.in(userId),
            QCartProduct.cartProduct.quantity.gt(QCartProduct.cartProduct.product.stock)
        )
        .fetch();
  }
}
