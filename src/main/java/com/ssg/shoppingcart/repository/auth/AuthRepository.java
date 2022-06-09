package com.ssg.shoppingcart.repository.auth;

import com.ssg.shoppingcart.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AuthRepository extends JpaRepository<User, Long>, AuthRepositoryCustom {

  User findByEmail(@Param("") String email);

  User findByUsername(@Param("") String username);

  User findByEmailAndPassword(@Param("") String email, @Param("") String password);
}
