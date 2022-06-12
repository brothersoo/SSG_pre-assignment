package com.ssg.shoppingcart.repository.order;

import static com.ssg.shoppingcart.domain.order.QOrder.order;
import static com.ssg.shoppingcart.domain.product.QOrderProduct.orderProduct;
import static com.ssg.shoppingcart.domain.product.QProduct.product;
import static com.ssg.shoppingcart.domain.product.QProductGroup.productGroup;
import static com.ssg.shoppingcart.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.shoppingcart.domain.order.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 주문 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  /**
   * 사용자의 모든 주문 내역을 검색
   * 연관된 주문 상품, 상품, 상품 그룹을 fetch join
   */
  @Override
  public List<Order> findAllOrdersByUserEmail(String userEmail) {
    return queryFactory
        .selectFrom(order).distinct()
        .join(order.orderProducts, orderProduct).fetchJoin()
        .join(orderProduct.product, product).fetchJoin()
        .join(product.productGroup, productGroup).fetchJoin()
        .join(order.user, user)
        .where(user.email.eq(userEmail))
        .orderBy(order.createdAt.desc())
        .fetch();
  }
}
