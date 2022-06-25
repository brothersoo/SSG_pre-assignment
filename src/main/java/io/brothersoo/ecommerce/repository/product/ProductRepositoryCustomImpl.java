package io.brothersoo.ecommerce.repository.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.brothersoo.ecommerce.dto.ProductDto.PriceRangeInGroups;
import io.brothersoo.ecommerce.dto.ProductDto.ProductInfo;
import io.brothersoo.ecommerce.dto.ProductDto.ProductListFilter;
import io.brothersoo.ecommerce.domain.product.QProduct;
import io.brothersoo.ecommerce.domain.product.QProductGroup;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/**
 * 상품 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  /**
   * 페이지네이트, 필터링 된 상품 리스트를 반환
   */
  @Override
  public Page<ProductInfo> findFilteredProducts(ProductListFilter filter, Pageable pageable) {
    BooleanBuilder condition = new BooleanBuilder();

    if (filter.getFromPrice() != null && filter.getToPrice() != null) {
      condition.and(QProduct.product.price.between(filter.getFromPrice(), filter.getToPrice()));
    }
    if (filter.getIsInStock() != null && filter.getIsInStock()) {
      condition.and(QProduct.product.stock.gt(0));
    } else if (filter.getIsOutOfStock() != null && filter.getIsOutOfStock()) {
      condition.and(QProduct.product.stock.eq(0));
    }
    if (filter.getGroupIds() != null) {
      condition.and(QProduct.product.productGroup.id.in(filter.getGroupIds()));
    }

    JPAQuery<ProductInfo> query = queryFactory
        .select(Projections.constructor(
            ProductInfo.class,
            QProduct.product.id, QProduct.product.name, QProduct.product.price,
            QProduct.product.stock,
            QProduct.product.productGroup.id, QProduct.product.productGroup.name))
        .from(QProduct.product)
        .join(QProduct.product.productGroup)
        .where(condition);

    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder(QProduct.product.getType(),
          QProduct.product.getMetadata());
      query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }

    query.offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    List<ProductInfo> results = query.fetch();

    Long count = queryFactory
        .select(QProduct.product.count())
        .from(QProduct.product)
        .where(condition)
        .fetchOne();

    return new PageImpl(results, pageable, count);
  }

  /**
   * 주어진 상품 그룹들 내 상품들의 최소, 최대 가격을 반환
   */
  @Override
  public PriceRangeInGroups getMinMaxPriceInProductGroups(List<Long> productGroupIds) {
    return queryFactory
        .select(Projections.constructor(
            PriceRangeInGroups.class, QProduct.product.price.min(), QProduct.product.price.max()
        ))
        .from(QProduct.product)
        .join(QProduct.product.productGroup, QProductGroup.productGroup)
        .where(QProduct.product.productGroup.id.in(productGroupIds))
        .fetchOne();
  }
}
