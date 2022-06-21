package com.ssg.shoppingcart.service.auth;

import static com.ssg.shoppingcart.domain.auth.RoleName.MEMBER;

import com.ssg.shoppingcart.domain.auth.Privilege;
import com.ssg.shoppingcart.domain.auth.Role;
import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.dto.AuthDto.LoginTokens;
import com.ssg.shoppingcart.dto.AuthDto.RegisterRequest;
import com.ssg.shoppingcart.dto.UserDto.UserInfo;
import com.ssg.shoppingcart.repository.user.UserRepository;
import com.ssg.shoppingcart.service.role.RoleService;
import com.ssg.shoppingcart.util.AuthUtil;
import com.ssg.shoppingcart.validator.UserValidator;
import java.util.ArrayList;
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

/**
 * 사용자 관련 서비스 및 사용자 인증에 사용될 서비스가 정의되어있는 클래스 입니다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;
  private final AuthUtil authUtil;
  private final UserValidator userValidator;
  private final ModelMapper modelMapper;

  /**
   * 이메일에 해당하는 유저를 반환합니다.<br/>
   * 해당하는 유저가 없을 시 에러를 발생합니다.
   */
  @Override
  public User findUserByEmailAndValidate(String userEmail) {
    User user = userRepository.findByEmailFetchPrivilege(userEmail);
    if (user == null) {
      throw new UsernameNotFoundException("user not found with the given email");
    }
    return user;
  }

  /**
   * Http Request의 Header에 담겨있는 token을 사용하여 사용자를 검색하는 로직입니다.
   */
  @Override
  public User findUserByHttpRequest(HttpServletRequest request) {
    String token = authUtil.isBearer(request);
    String userEmail = authUtil.decodeJWT(token).getSubject();

    return findUserByEmailAndValidate(userEmail);
  }

  /**
   * Spring security의 UserDetailService에서 오버라이딩 한 사용자 인증에 사용되는 메서드입니다.<br/>
   * 사용자의 권한 목록을 담은 UserDetails를 반환합니다.
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = findUserByEmailAndValidate(email);

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (String privilegeName : user.getAllPrivilegeNames()) {
      authorities.add(new SimpleGrantedAuthority(privilegeName));
    }

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), authorities
    );
  }

  /**
   * 회원가입을 진행하는 로직입니다.<br/>
   * 비밀번호 확인, 이메일 중복, 유저명 중복을 검증합니다.<br/>
   * 지정되어 있지 않다면 User Type은 Member로 설정됩니다.<br/>
   * MEMBER role을 추가합니다.
   */
  @Override
  @Transactional
  public UserInfo register(RegisterRequest registerData) {
    User userWithRequestEmail = userRepository.findByEmail(registerData.getEmail());
    User userWithRequestUsername = userRepository.findByUsername(registerData.getUsername());
    userValidator.registerValidate(registerData, userWithRequestEmail, userWithRequestUsername);
    User user = User.builder()
        .email(registerData.getEmail())
        .password(passwordEncoder.encode(registerData.getPassword()))
        .username(registerData.getUsername())
        .build();
    roleService.grantRoleToUser(user, MEMBER.name());
    return modelMapper.map(userRepository.save(user), UserInfo.class);
  }

  /**
   * Refresh token을 사용하여 새로운 access token을 발행하는 서비스 로직입니다.
   */
  @Override
  public LoginTokens refreshToken(HttpServletRequest request) {
    User user = findUserByHttpRequest(request);
    String issuer = request.getRequestURL().toString();

    String accessToken = authUtil.generateAccessToken(
        user.getEmail(), user.getAllPrivilegeNames(), issuer
    );
    String refreshToken = authUtil.generateRefreshToken(user.getEmail(), issuer);
    return new LoginTokens(accessToken, refreshToken);
  }

  @Override
  public Role saveRole(String roleName) {
    return null;
  }

  @Override
  public void grantRoleToUser(String email, String roleName) {

  }

  @Override
  public Privilege savePrivilege(String privilegeName) {
    return null;
  }

  @Override
  public void grantPrivilegeToRole(String roleName, String privilegeName) {

  }
}
