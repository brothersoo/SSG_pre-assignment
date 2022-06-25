package io.brothersoo.ecommerce.service.role;

import io.brothersoo.ecommerce.domain.auth.Role;
import io.brothersoo.ecommerce.domain.auth.UserRole;
import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.repository.role.RoleRepository;
import io.brothersoo.ecommerce.repository.role.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final UserRoleRepository userRoleRepository;

  /**
   * 이름에 해당하는 Role을 반환합니다.
   */
  @Override
  public Role findRoleByName(String roleName) {
    Role role = roleRepository.findByName("ROLE_" + roleName);
    if (role == null) {
      throw new IllegalArgumentException("invalid role name");
    }
    return role;
  }

  /**
   * 지정한 사용자에게 role을 부여합니다.
   */
  @Override
  @Transactional
  public Long grantRoleToUser(User user, String roleName) {
    Role role = findRoleByName(roleName);
    UserRole userRole = UserRole.builder().user(user).role(role).build();
    UserRole persistedRole = userRoleRepository.save(userRole);
    return persistedRole.getId();
  }
}
