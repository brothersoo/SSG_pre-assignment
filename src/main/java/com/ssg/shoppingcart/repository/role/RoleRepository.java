package com.ssg.shoppingcart.repository.role;

import com.ssg.shoppingcart.domain.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

  public Role findByName(@Param("") String name);
}
