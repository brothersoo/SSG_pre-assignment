package com.ssg.shoppingcart.repository.user;

import com.ssg.shoppingcart.domain.user.User;

public interface UserRepositoryCustom {

  User findByEmailWithPrivilege(String email);
}
