package io.brothersoo.ecommerce.controller;

import io.brothersoo.ecommerce.dto.AuthDto.LoginTokens;
import io.brothersoo.ecommerce.dto.AuthDto.RegisterRequest;
import io.brothersoo.ecommerce.dto.UserDto.UserInfo;
import io.brothersoo.ecommerce.service.auth.AuthService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 인증에 관련 controller
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  /**
   * 회원가입을 위한 controller입니다.
   *
   * @param registerData email, password, passwordConfirm, username을 필요로 합니다.
   * @return 회원으로 등록된 정보를 반환합니다.
   */
  @PostMapping("/register")
  public ResponseEntity<UserInfo> userRegister(@RequestBody RegisterRequest registerData) {
    UserInfo user = authService.register(registerData);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  /**
   * refresh token을 사용하여 새로운 access token을 발급받는데 사용되는 controller입니다.
   *
   * @param request refresh token이 담길 header를 받을 request입니다.
   * @return 새로 발급받은 access token과 기존의 refresh token을 반환합니다.
   */
  @GetMapping("/token/refresh")
  public ResponseEntity<LoginTokens> refreshToken(HttpServletRequest request) {
    LoginTokens tokens = authService.refreshToken(request);
    return new ResponseEntity<>(tokens, HttpStatus.OK);
  }
}
