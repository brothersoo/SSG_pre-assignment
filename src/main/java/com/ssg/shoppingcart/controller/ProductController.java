package com.ssg.shoppingcart.controller;

import com.ssg.shoppingcart.dto.ProductDto.PriceRangeInGroups;
import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import com.ssg.shoppingcart.dto.ProductDto.ProductListFilter;
import com.ssg.shoppingcart.service.product.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  /**
   * 필터링된 상품 리스트를 불러옵니다.
   *
   * @param filter   필터링에 사용될 정보들. 가격 범위, 그룹 id, 재고 상태를 포함
   * @param pageable 페이지네이션을 위한 param
   * @return 필터링 / 페이지네이팅 된 상품 정보 리스트를 반환합니다.
   */
  @GetMapping
  public ResponseEntity<Page<ProductInfo>> filteredProductList(
      ProductListFilter filter, @PageableDefault Pageable pageable
  ) {
    return new ResponseEntity<>(
        productService.findFilteredProducts(filter, pageable), HttpStatus.OK
    );
  }

  /**
   * 선택한 그룹 내의 상품들의 가격 중 최소, 최대값을 불러옵니다.
   *
   * @param groupIds 선택한 그룹들 id
   * @return 조건 내 최소, 최대 가격을 반환합니다.
   */
  @GetMapping("/price_range")
  public ResponseEntity<PriceRangeInGroups> priceRangeInGroupsRetrieve(
      @RequestParam("groupIds") List<Long> groupIds
  ) {
    return new ResponseEntity<>(
        productService.getMinMaxPriceInProductGroups(groupIds),
        HttpStatus.OK
    );
  }
}
