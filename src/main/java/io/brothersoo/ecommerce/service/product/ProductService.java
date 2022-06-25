package io.brothersoo.ecommerce.service.product;

import io.brothersoo.ecommerce.domain.product.Product;
import io.brothersoo.ecommerce.dto.ProductDto.PriceRangeInGroups;
import io.brothersoo.ecommerce.dto.ProductDto.ProductInfo;
import io.brothersoo.ecommerce.dto.ProductDto.ProductListFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  Product findByIdAndValidate(Long id);

  Page<ProductInfo> findFilteredProducts(ProductListFilter filter, Pageable pageable);

  PriceRangeInGroups getMinMaxPriceInProductGroups(List<Long> productGroupIds);
}
