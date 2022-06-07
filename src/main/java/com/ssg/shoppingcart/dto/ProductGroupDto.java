package com.ssg.shoppingcart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductGroupDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class ProductGroupInfo {

    private Long id;
    private String name;
  }
}
