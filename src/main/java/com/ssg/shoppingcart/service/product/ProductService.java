package com.ssg.shoppingcart.service.product;

import com.ssg.shoppingcart.dto.ProductDto.PriceRangeInGroups;
import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import com.ssg.shoppingcart.dto.ProductDto.ProductListFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  Page<ProductInfo> findFilteredProducts(ProductListFilter filter, Pageable pageable);

  PriceRangeInGroups getMinMaxPriceInProductGroups(List<Long> productGroupIds);
}
