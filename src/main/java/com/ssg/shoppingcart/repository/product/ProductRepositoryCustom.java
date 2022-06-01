package com.ssg.shoppingcart.repository.product;

import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import com.ssg.shoppingcart.dto.ProductDto.ProductListFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

  Page<ProductInfo> findFilteredProducts(ProductListFilter filter, Pageable pageable);
}
