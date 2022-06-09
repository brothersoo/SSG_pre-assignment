package com.ssg.shoppingcart.filter.auth;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.shoppingcart.dto.AuthDto.LoginTokens;
import com.ssg.shoppingcart.util.AuthUtil;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final AuthUtil authUtil;

  public CustomAuthenticationFilter(
      AuthenticationManager authenticationManager, AuthUtil authUtil
  ) {
    this.authenticationManager = authenticationManager;
    this.setFilterProcessesUrl("/auth/login");
    this.authUtil = authUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(email, password);
    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException, ServletException {
    User user = (User) authentication.getPrincipal();
    String subject = user.getUsername();
    List<String> roles = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    String issuer = request.getRequestURL().toString();
    Date accessExpireDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    Date refreshExpireDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
    String accessToken
        = authUtil.generateAccessToken(subject, roles, issuer, accessExpireDate);
    String refreshToken = authUtil.generateRefreshToken(subject, issuer, refreshExpireDate);
    LoginTokens tokens = new LoginTokens(accessToken, refreshToken);
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    super.unsuccessfulAuthentication(request, response, failed);
  }
}
