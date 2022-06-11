package com.ssg.shoppingcart.repository.order;

import static com.ssg.shoppingcart.domain.QOrder.order;
import static com.ssg.shoppingcart.domain.QOrderProduct.orderProduct;
import static com.ssg.shoppingcart.domain.QProduct.product;
import static com.ssg.shoppingcart.domain.QProductGroup.productGroup;
import static com.ssg.shoppingcart.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.shoppingcart.domain.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Order> findAllOrdersByUserEmail(String userEmail) {
    return queryFactory
        .selectFrom(order).distinct()
        .join(order.orderProducts, orderProduct)
        .fetchJoin()
        .join(orderProduct.product, product)
        .fetchJoin()
        .join(product.productGroup, productGroup)
        .fetchJoin()
        .join(order.user, user)
        .where(user.email.eq(userEmail))
        .orderBy(order.createdAt.desc())
        .fetch();
  }
}
