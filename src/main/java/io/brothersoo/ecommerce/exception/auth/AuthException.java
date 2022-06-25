package io.brothersoo.ecommerce.exception.auth;

/**
 * 사용자 인증에 실패했을 때 발생하는 예외
 */
public class AuthException extends RuntimeException {

  public AuthException(String message) {
    super(message);
  }
}
