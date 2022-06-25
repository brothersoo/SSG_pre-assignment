package io.brothersoo.ecommerce.repository.product;

import io.brothersoo.ecommerce.dto.ProductDto.PriceRangeInGroups;
import io.brothersoo.ecommerce.dto.ProductDto.ProductInfo;
import io.brothersoo.ecommerce.dto.ProductDto.ProductListFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

  Page<ProductInfo> findFilteredProducts(ProductListFilter filter, Pageable pageable);

  PriceRangeInGroups getMinMaxPriceInProductGroups(List<Long> productGroupIds);
}
