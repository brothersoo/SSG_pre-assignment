package io.brothersoo.ecommerce.util;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.brothersoo.ecommerce.exception.auth.AuthException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 사용자 인증에 필요한 기능 함수 집합 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class AuthUtil {

  private final Algorithm jwtAlgorithm;

  /**
   * 암호화된 토큰의 정보를 사용하여 새로운 토큰을 발행합니다.
   *
   * @param encryptedToken 암호화된 토큰
   * @param expireDate     만료 기간
   * @return 새로운 JWT
   */
  public String generateToken(String encryptedToken, Date expireDate) {
    DecodedJWT decodedJWT = this.decodeJWT(encryptedToken);
    return JWT.create()
        .withSubject(decodedJWT.getSubject())
        .withExpiresAt(expireDate)
        .withIssuer(decodedJWT.getIssuer())
        .sign(jwtAlgorithm);
  }

  /**
   * 인자들을 사용하여 새로운 access token을 발행합니다.
   * 토큰의 만료 시간은 발행일 + 30분입니다.
   *
   * @param subject token payload의 subject (사용자 email)
   * @param roles   token 주인의 role
   * @param issuer  token 발행자
   * @return 새로운 JWT
   */
  public String generateAccessToken(String subject, List<String> roles, String issuer) {
    return JWT.create()
        .withSubject(subject)
        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
        .withIssuer(issuer)
        .withClaim("roles", roles)
        .sign(jwtAlgorithm);
  }

  /**
   * 인자들을 사용하여 새로운 refresh token을 발행합니다.
   * 토큰의 만료 기간은 발행일 + 20일 입니다.
   *
   * @param subject token payload의 subject (사용자 email)
   * @param issuer  token 발행자
   * @return 새로운 JWT
   */
  public String generateRefreshToken(String subject, String issuer) {
    return JWT.create()
        .withSubject(subject)
        .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 24 * 60 * 60 * 1000))
        .withIssuer(issuer)
        .sign(jwtAlgorithm);
  }

  /**
   * 암호화된 토큰을 복호화합니다.
   *
   * @param jwt 암호화된 JWT
   * @return 복호화된 토큰
   */
  public DecodedJWT decodeJWT(String jwt) {
    JWTVerifier verifier = JWT.require(jwtAlgorithm).build();
    return verifier.verify(jwt);
  }

  /**
   * Bearer 토큰인지 검증합니다.
   *
   * @param request header에 담길 token을 위한 http request
   * @return Bearer prefix를 제거한 token
   */
  public String isBearer(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring("Bearer ".length());
    } else {
      throw new AuthException("invalid bearer token");
    }
  }
}
