package io.brothersoo.ecommerce.repository.user;

import static io.brothersoo.ecommerce.domain.auth.QUserRole.userRole;
import static io.brothersoo.ecommerce.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.domain.auth.QPrivilege;
import io.brothersoo.ecommerce.domain.auth.QRole;
import io.brothersoo.ecommerce.domain.auth.QRolePrivilege;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  /**
   * 사용자 이메일을 통하여 검색
   * 연관된 role 과 privilege를 fetch join
   */
  @Override
  public User findByEmailFetchPrivilege(String email) {
    return queryFactory.selectFrom(user)
        .join(user.userRoles, userRole).fetchJoin()
        .join(userRole.role, QRole.role).fetchJoin()
        .join(QRole.role.rolePrivileges, QRolePrivilege.rolePrivilege).fetchJoin()
        .join(QRolePrivilege.rolePrivilege.privilege, QPrivilege.privilege).fetchJoin()
        .where(user.email.eq(email))
        .fetchOne();
  }
}
