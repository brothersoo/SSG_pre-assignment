package com.ssg.shoppingcart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class UserInfo {

    private Long id;
    private String email;
    private String username;
  }
}
