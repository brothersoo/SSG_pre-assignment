package io.brothersoo.ecommerce.filter.auth;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.brothersoo.ecommerce.dto.AuthDto.LoginTokens;
import io.brothersoo.ecommerce.util.AuthUtil;
import java.io.IOException;
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

/**
 * 사용자 인증 처리에 사용되는 필터
 */
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

  /**
   * 사용자 인증에 사용될 데이터 필드의 정확한 이름을 명시 후 검증
   */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response
  ) throws AuthenticationException {
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(email, password);
    return authenticationManager.authenticate(authenticationToken);
  }

  /**
   * 사용자 인증에 성공 시 처리할 로직
   * JWT에 들어갈 payload를 적재 후 반환
   */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication
  ) throws IOException, ServletException {
    User user = (User) authentication.getPrincipal();
    String subject = user.getUsername();
    List<String> roles = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    String issuer = request.getRequestURL().toString();
    String accessToken
        = authUtil.generateAccessToken(subject, roles, issuer);
    String refreshToken = authUtil.generateRefreshToken(subject, issuer);
    LoginTokens tokens = new LoginTokens(accessToken, refreshToken);
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }

  /**
   * 사용자 인증에 실패 시 처리할 로직
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    super.unsuccessfulAuthentication(request, response, failed);
  }
}
