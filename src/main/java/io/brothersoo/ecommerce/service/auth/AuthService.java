package io.brothersoo.ecommerce.service.auth;

import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.domain.auth.Privilege;
import io.brothersoo.ecommerce.domain.auth.Role;
import io.brothersoo.ecommerce.dto.AuthDto.LoginTokens;
import io.brothersoo.ecommerce.dto.AuthDto.RegisterRequest;
import io.brothersoo.ecommerce.dto.UserDto.UserInfo;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

  User findUserByEmailAndValidate(String userEmail);

  User findUserByHttpRequest(HttpServletRequest request);

  UserInfo register(RegisterRequest registerData);

  LoginTokens refreshToken(HttpServletRequest request);

  Role saveRole(String roleName);

  void grantRoleToUser(String email, String roleName);

  Privilege savePrivilege(String privilegeName);

  void grantPrivilegeToRole(String roleName, String privilegeName);
}
