package io.brothersoo.ecommerce.repository.user;

import io.brothersoo.ecommerce.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  User findByEmail(@Param("") String email);

  User findByUsername(@Param("") String username);
}
