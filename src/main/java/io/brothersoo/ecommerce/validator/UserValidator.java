package io.brothersoo.ecommerce.validator;

import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.AuthDto.RegisterRequest;
import org.springframework.stereotype.Component;

/**
 * 사용자 서비스 내 검증
 */
@Component
public class UserValidator {

  /**
   * 회원가입 로직 내 검증
   * 비밀번호와 비밀번호 확인이 동일한지, 중복 이메일이 있는지, 중복 유저명이 있는지 검증
   */
  public void registerValidate(
      RegisterRequest registerData, User userWithRequestEmail, User userWithRequestUsername
  ) {
    if (!registerData.getPassword().equals(registerData.getPasswordConfirm())) {
      throw new IllegalArgumentException("password confirm doesn't match");
    }
    if (userWithRequestEmail != null) {
      throw new IllegalArgumentException("already existing email");
    }
    if (userWithRequestUsername != null) {
      throw new IllegalArgumentException("already existing username");
    }
  }
}
