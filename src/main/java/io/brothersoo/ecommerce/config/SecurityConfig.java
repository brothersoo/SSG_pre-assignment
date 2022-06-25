package io.brothersoo.ecommerce.config;

import io.brothersoo.ecommerce.filter.auth.CustomAuthenticationFilter;
import io.brothersoo.ecommerce.filter.auth.CustomAuthorizationFilter;
import io.brothersoo.ecommerce.util.AuthUtil;
import com.sun.tools.javac.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * 사용자 인증을 위해 사용한 Spring Security를 위한 설정 클래스
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final AuthUtil authUtil;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * UserDetailsService을 상속받은 AuthServiceImpl을 사용자 인증에 사용할 서비스로 지정합니다.
   * 비밀번호 암호화에 BCryptPasswordEncoder를 사용할 것으로 지정합니다.
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(bCryptPasswordEncoder);
  }

  /**
   * http 통신에서의 보안 수준 및 데이터를 설정합니다.
   * 인증에서의 cors 설정, role 및 privilege를 사용한 서비스 허가를 설정합니다.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors().configurationSource(request -> {
          CorsConfiguration cors = new CorsConfiguration();
          cors.setAllowedOrigins(List.of("http://localhost:8080"));
          cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          cors.setAllowedHeaders(List.of("*"));
          return cors;
        })
        .and()
        .authorizeHttpRequests()
        .antMatchers("/cart_product*").hasAuthority("USE_CART")
        .antMatchers("/order*").hasAuthority("ORDER")
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/auth/**", "/product*").permitAll()
        .anyRequest().permitAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(
            new CustomAuthenticationFilter(authenticationManagerBean(), authUtil))
        .addFilterBefore(
            new CustomAuthorizationFilter(authUtil), UsernamePasswordAuthenticationFilter.class
        );
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
