package com.ssg.shoppingcart.dto;

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
    private List<String> groupNames;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class ProductInfo {

    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private String productGroupName;
  }
}
