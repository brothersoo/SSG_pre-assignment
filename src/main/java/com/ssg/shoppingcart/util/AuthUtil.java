package com.ssg.shoppingcart.util;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ssg.shoppingcart.exception.auth.AuthException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

  private static String jwtSecret;

  public static Algorithm getAlgorithm() {
    return AlgorithmHolder.algorithm;
  }

  @Value("${jwt.secret}")
  private void setJwtSecret(String value) {
    jwtSecret = value;
  }

  public String generateToken(String token, Date expireDate) {
    DecodedJWT decodedJWT = this.decodeJWT(token);
    return JWT.create()
        .withSubject(decodedJWT.getSubject())
        .withExpiresAt(expireDate)
        .withIssuer(decodedJWT.getIssuer())
        .sign(AuthUtil.getAlgorithm());
  }

  public String generateAccessToken(
      String subject, List<String> roles, String issuer, Date expireDate
  ) {
    return JWT.create()
        .withSubject(subject)
        .withExpiresAt(expireDate)
        .withIssuer(issuer)
        .withClaim("roles", roles)
        .sign(AuthUtil.getAlgorithm());
  }

  public String generateRefreshToken(String subject, String issuer, Date expireDate) {
    return JWT.create()
        .withSubject(subject)
        .withExpiresAt(expireDate)
        .withIssuer(issuer)
        .sign(AuthUtil.getAlgorithm());
  }

  public DecodedJWT decodeJWT(String jwt) {
    Algorithm algorithm = AuthUtil.getAlgorithm();
    JWTVerifier verifier = JWT.require(algorithm).build();
    return verifier.verify(jwt);
  }

  public String isBearer(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring("Bearer ".length());
    } else {
      throw new AuthException("invalid bearer token");
    }
  }

  private static class AlgorithmHolder {

    private static final Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
  }
}
