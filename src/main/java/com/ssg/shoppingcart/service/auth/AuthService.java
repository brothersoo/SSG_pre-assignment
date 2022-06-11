package com.ssg.shoppingcart.service.auth;

import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.domain.auth.Privilege;
import com.ssg.shoppingcart.domain.auth.Role;
import com.ssg.shoppingcart.dto.AuthDto.LoginTokens;
import com.ssg.shoppingcart.dto.AuthDto.RegisterRequest;
import com.ssg.shoppingcart.dto.UserDto.UserInfo;
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
