package com.ssg.shoppingcart.validator;

import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.dto.AuthDto.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

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
