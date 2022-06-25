package io.brothersoo.ecommerce.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecretConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Algorithm algorithm() {
    return Algorithm.HMAC256(jwtSecret.getBytes());
  }
}
