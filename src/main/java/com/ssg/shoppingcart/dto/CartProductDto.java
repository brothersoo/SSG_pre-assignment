package com.ssg.shoppingcart.dto;

import com.ssg.shoppingcart.dto.ProductDto.ProductInfo;
import com.ssg.shoppingcart.dto.UserDto.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CartProductDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class CartProductAddRequest {

    private String userEmail;
    private int quantity;
    private Boolean addingIsConfirmed;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class CartProductInfo {

    private Long id;
    private Integer quantity;
    private ProductInfo product;
    private UserInfo user;
  }
}
