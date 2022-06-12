package com.ssg.shoppingcart.filter.auth;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssg.shoppingcart.util.AuthUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 사용자 인가 처리에 사용되는 필터
 */
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

  private final AuthUtil authUtil;

  public CustomAuthorizationFilter(AuthUtil authUtil) {
    this.authUtil = authUtil;
  }

  /**
   * 인가가 필요한 path는 서비스를 요청한 token에 포함된 role을 검사 후 올바를 권한이 있는지 필터링 진행
   *
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (
        request.getServletPath().startsWith("/auth")
            || request.getServletPath().startsWith("/product")
    ) {
      filterChain.doFilter(request, response);
    } else {
      try {
        String token = authUtil.isBearer(request);
        if (token != null) {
          DecodedJWT decodedJWT = authUtil.decodeJWT(token);
          String email = decodedJWT.getSubject();
          String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
          Arrays.stream(roles).forEach(role ->
              authorities.add(new SimpleGrantedAuthority(role)));
          UsernamePasswordAuthenticationToken authenticationToken
              = new UsernamePasswordAuthenticationToken(email, null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
      } catch (Exception exception) {
        log.error("Error loggin in: {}", exception.getMessage());
        response.setHeader("error", exception.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    }
  }
}
