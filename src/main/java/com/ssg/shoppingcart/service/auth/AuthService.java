package com.ssg.shoppingcart.service.auth;

import com.ssg.shoppingcart.domain.User;
import com.ssg.shoppingcart.domain.auth.Privilege;
import com.ssg.shoppingcart.domain.auth.Role;
import com.ssg.shoppingcart.dto.AuthDto.LoginTokens;
import com.ssg.shoppingcart.dto.AuthDto.RegisterRequest;
import com.ssg.shoppingcart.dto.UserDto.UserInfo;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

  UserInfo register(RegisterRequest registerData);

  User getUserByEmail(String email);

  LoginTokens refreshToken(HttpServletRequest request);

  Role saveRole(String roleName);

  void GrantRoleToUser(String email, String roleName);

  Privilege savePrivilege(String privilegeName);

  void GrantPrivilegeToRole(String roleName, String privilegeName);
}
