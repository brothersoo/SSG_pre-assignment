package io.brothersoo.ecommerce.repository.user;

import io.brothersoo.ecommerce.domain.user.User;

public interface UserRepositoryCustom {

  User findByEmailFetchPrivilege(String email);
}
