package com.ssg.shoppingcart.repository.role;

import com.ssg.shoppingcart.domain.auth.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
