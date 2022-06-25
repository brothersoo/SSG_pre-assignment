package io.brothersoo.ecommerce.service.role;

import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.domain.auth.Role;

public interface RoleService {

  Role findRoleByName(String roleName);

  Long grantRoleToUser(User user, String roleName);
}
