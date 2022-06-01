package com.ssg.shoppingcart.repository.product;

import static com.ssg.shoppingcart.domain.QProduct.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    if (filter.getGroupNames() != null) {
      condition.and(product.productGroup.name.in(filter.getGroupNames()));
    }

    JPAQuery<ProductInfo> query = queryFactory
        .select(Projections.constructor(
            ProductInfo.class,
            product.id, product.name, product.price, product.stock))
        .from(product)
        .where(condition)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder(product.getType(), product.getMetadata());
      query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }

    List<ProductInfo> results = query.fetch();

    return new PageImpl(results, pageable, results.size());
  }
}
