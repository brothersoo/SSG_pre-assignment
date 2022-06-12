package com.ssg.shoppingcart.dto;

import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CartProductDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class CartProductAddRequest {

    private int quantity;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class CartProductQuantityModifyRequest {

    private Integer quantity;
  }

  @NoArgsConstructor
  @Data
  public static class CartProductInfo {

    private Long id;
    private Integer quantity;
    private ProductInfo product;

    public CartProductInfo(Long id, Integer quantity, Long productId, String productName,
        Integer productPrice, Integer productStock, Long productGroupId, String productGroupName) {
      this.id = id;
      this.quantity = quantity;
      this.product = new ProductInfo(productId, productName,
          productPrice, productStock, productGroupId, productGroupName);
    }

    public boolean isOrderableQuantity() {
      return quantity > product.getStock();
    }
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class QuantityResetRequest {

    private String type;
  }
}
