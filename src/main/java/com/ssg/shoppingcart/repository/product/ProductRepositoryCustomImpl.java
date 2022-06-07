package com.ssg.shoppingcart.repository.product;

import static com.ssg.shoppingcart.domain.QProduct.product;
import static com.ssg.shoppingcart.domain.QProductGroup.productGroup;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.shoppingcart.dto.ProductDto.PriceRangeInGroups;
import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import com.ssg.shoppingcart.dto.ProductDto.ProductListFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<ProductInfo> findFilteredProducts(ProductListFilter filter, Pageable pageable) {
    BooleanBuilder condition = new BooleanBuilder();

    if (filter.getFromPrice() != null && filter.getToPrice() != null) {
      condition.and(product.price.between(filter.getFromPrice(), filter.getToPrice()));
    }
    if (filter.getIsInStock() != null && filter.getIsInStock()) {
      condition.and(product.stock.gt(0));
    } else if (filter.getIsOutOfStock() != null && filter.getIsOutOfStock()) {
      condition.and(product.stock.eq(0));
    }
    if (filter.getGroupIds() != null) {
      condition.and(product.productGroup.id.in(filter.getGroupIds()));
    }

    JPAQuery<ProductInfo> query = queryFactory
        .select(Projections.constructor(
            ProductInfo.class,
            product.id, product.name, product.price, product.stock,
            product.productGroup.id, product.productGroup.name))
        .from(product)
        .join(product.productGroup)
        .where(condition);

    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder(product.getType(), product.getMetadata());
      query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }

    query.offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    List<ProductInfo> results = query.fetch();

    Long count = queryFactory
        .select(product.count())
        .from(product)
        .where(condition)
        .fetchOne();

    return new PageImpl(results, pageable, count);
  }

  @Override
  public PriceRangeInGroups getMinMaxPriceInProductGroups(List<Long> productGroupIds) {
    return queryFactory
        .select(Projections.constructor(
            PriceRangeInGroups.class, product.price.min(), product.price.max()
        ))
        .from(product)
        .join(product.productGroup, productGroup)
        .where(product.productGroup.id.in(productGroupIds))
        .fetchOne();
  }
}
