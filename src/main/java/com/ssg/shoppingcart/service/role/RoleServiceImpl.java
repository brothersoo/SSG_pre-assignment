package com.ssg.shoppingcart.service.role;

import com.ssg.shoppingcart.domain.auth.Role;
import com.ssg.shoppingcart.domain.auth.UserRole;
import com.ssg.shoppingcart.domain.user.User;
import com.ssg.shoppingcart.repository.role.RoleRepository;
import com.ssg.shoppingcart.repository.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final UserRoleRepository userRoleRepository;

  @Override
  public Role findRoleByName(String roleName) {
    Role role = roleRepository.findByName("ROLE_" + roleName);
    if (role == null) {
      throw new IllegalArgumentException("invalid role name");
    }
    return role;
  }

  @Override
  public Long grantRoleToUser(User user, String roleName) {
    Role role = findRoleByName(roleName);
    UserRole userRole = UserRole.builder().user(user).role(role).build();
    UserRole persistedRole = userRoleRepository.save(userRole);
    return persistedRole.getId();
  }
}
