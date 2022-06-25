package io.brothersoo.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class RegisterRequest {

    private String email;
    private String password;
    private String passwordConfirm;
    private String username;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class LoginRequest {

    private String email;
    private String password;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class LoginTokens {

    private String accessToken;
    private String refreshToken;
  }
}
