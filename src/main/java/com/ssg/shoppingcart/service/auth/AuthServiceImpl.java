package com.ssg.shoppingcart.service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ssg.shoppingcart.domain.User;
import com.ssg.shoppingcart.domain.auth.Privilege;
import com.ssg.shoppingcart.domain.auth.Role;
import com.ssg.shoppingcart.dto.AuthDto.LoginTokens;
import com.ssg.shoppingcart.dto.AuthDto.RegisterRequest;
import com.ssg.shoppingcart.dto.UserDto.UserInfo;
import com.ssg.shoppingcart.repository.auth.AuthRepository;
import com.ssg.shoppingcart.util.AuthUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {

  private final AuthRepository authRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthUtil authUtil;
  private final ModelMapper modelMapper;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = authRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("user not found with the given email");
    }

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getUserRoles().forEach(userRole ->
        userRole.getRole().getRolePrivileges().forEach(rolePrivilege ->
            authorities.add(
                new SimpleGrantedAuthority(rolePrivilege.getPrivilege().getName())
            )
        )
    );

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), authorities);
  }

  @Override
  public UserInfo register(RegisterRequest registerData) {
    if (!registerData.getPassword().equals(registerData.getPasswordConfirm())) {
      throw new IllegalArgumentException("password confirm doesn't match");
    }
    if (authRepository.findByEmail(registerData.getEmail()) != null) {
      throw new IllegalArgumentException("already existing email");
    }
    if (authRepository.findByUsername(registerData.getUsername()) != null) {
      throw new IllegalArgumentException("already existing username");
    }
    User user = User.builder()
        .email(registerData.getEmail())
        .password(passwordEncoder.encode(registerData.getPassword()))
        .username(registerData.getUsername())
        .build();

    return modelMapper.map(authRepository.save(user), UserInfo.class);
  }

  @Override
  public User getUserByEmail(String email) {
    return authRepository.findByEmail(email);
  }

  @Override
  public LoginTokens refreshToken(HttpServletRequest request) {
    String refreshToken = authUtil.isBearer(request);
    DecodedJWT decodedJWT = authUtil.decodeJWT(refreshToken);
    String issuer = request.getRequestURL().toString();
    User user = getUserByEmail(decodedJWT.getSubject());
    Date expireDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);

    String accessToken = authUtil.generateAccessToken(
        user.getEmail(), user.getAllPrivilegeNames(), issuer, expireDate
    );
    return new LoginTokens(accessToken, refreshToken);
  }

  @Override
  public Role saveRole(String roleName) {
    return null;
  }

  @Override
  public void GrantRoleToUser(String email, String roleName) {

  }

  @Override
  public Privilege savePrivilege(String privilegeName) {
    return null;
  }

  @Override
  public void GrantPrivilegeToRole(String roleName, String privilegeName) {

  }
}
