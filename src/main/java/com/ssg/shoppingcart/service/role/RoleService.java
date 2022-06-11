package com.ssg.shoppingcart.service.role;

import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.domain.auth.Role;

public interface RoleService {

  Role findRoleByName(String roleName);

  Long grantRoleToUser(User user, String roleName);
}
