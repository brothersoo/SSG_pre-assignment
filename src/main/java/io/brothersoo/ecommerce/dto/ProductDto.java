package io.brothersoo.ecommerce.dto;

import io.brothersoo.ecommerce.dto.ProductGroupDto.ProductGroupInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductDto {

  @AllArgsConstructor
  @Data
  public static class ProductListFilter {

    private Integer fromPrice;
    private Integer toPrice;
    private Boolean isInStock;
    private Boolean isOutOfStock;
    private List<Long> groupIds;
  }

  @NoArgsConstructor
  @Data
  public static class ProductInfo {

    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private ProductGroupInfo productGroup;

    public ProductInfo(Long id, String name, Integer price, Integer stock,
        Long productGroupId, String productGroupName) {
      this.id = id;
      this.name = name;
      this.price = price;
      this.stock = stock;
      this.productGroup = new ProductGroupInfo(productGroupId, productGroupName);
    }
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class PriceRangeInGroups {

    private Integer minPrice;
    private Integer maxPrice;
  }
}
