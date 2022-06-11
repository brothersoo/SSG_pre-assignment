package com.ssg.shoppingcart.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.shoppingcart.dto.AuthDto.LoginTokens;
import com.ssg.shoppingcart.dto.AuthDto.RegisterRequest;
import com.ssg.shoppingcart.dto.UserDto.UserInfo;
import com.ssg.shoppingcart.service.auth.AuthService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<UserInfo> userRegister(@RequestBody RegisterRequest registerData) {
    UserInfo user = authService.register(registerData);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @GetMapping("/token/refresh")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    LoginTokens tokens = authService.refreshToken(request);
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
