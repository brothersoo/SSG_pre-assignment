package io.brothersoo.ecommerce.repository.role;

import io.brothersoo.ecommerce.domain.auth.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
