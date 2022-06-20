package com.ssg.shoppingcart.service.product;

import com.ssg.shoppingcart.domain.product.Product;
import com.ssg.shoppingcart.dto.ProductDto.PriceRangeInGroups;
import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import com.ssg.shoppingcart.dto.ProductDto.ProductListFilter;
import com.ssg.shoppingcart.repository.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  /**
   * id를 사용하여 상품을 검색합니다.<br/>
   * id에 해당하는 상품이 없을 시 에러를 발생합니다.
   */
  @Override
  public Product findByIdAndValidate(Long productId) {
    Optional<Product> optionalProduct = productRepository.findById(productId);
    if (!optionalProduct.isPresent()) {
      throw new IllegalArgumentException("no such product with the given id");
    }
    return optionalProduct.get();
  }

  /**
   * 필터링 된 상품 리스트를 반환합니다.<br/>
   * 가격 범위, 상품 그룹, 정렬 기준을 필터에 정의할 수 있습니다.
   */
  @Override
  public Page<ProductInfo> findFilteredProducts(ProductListFilter filter, Pageable pageable) {
    pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
        pageable.getPageSize(),
        pageable.getSort());
    return productRepository.findFilteredProducts(filter, pageable);
  }

  /**
   * 선택된 상품 그룹 내의 상품들 중 최소 가격, 최대 가격을 반환합니다.<br/>
   * 프런트의 상품 가격 범위 슬라이더를 위해 사용됩니다.
   */
  @Override
  public PriceRangeInGroups getMinMaxPriceInProductGroups(List<Long> productGroupIds) {
    return productRepository.getMinMaxPriceInProductGroups(productGroupIds);
  }
}
