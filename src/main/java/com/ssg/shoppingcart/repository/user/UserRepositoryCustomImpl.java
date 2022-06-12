package com.ssg.shoppingcart.repository.user;

import static com.ssg.shoppingcart.domain.auth.QPrivilege.privilege;
import static com.ssg.shoppingcart.domain.auth.QRole.role;
import static com.ssg.shoppingcart.domain.auth.QRolePrivilege.rolePrivilege;
import static com.ssg.shoppingcart.domain.auth.QUserRole.userRole;
import static com.ssg.shoppingcart.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssg.shoppingcart.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private JPAQueryFactory queryFactory;

  /**
   * 사용자 이메일을 통하여 검색
   * 연관된 role 과 privilege를 fetch join
   */
  @Override
  public User findByEmailFetchPrivilege(String email) {
    return queryFactory.selectFrom(user)
        .join(user.userRoles, userRole).fetchJoin()
        .join(userRole.role, role).fetchJoin()
        .join(role.rolePrivileges, rolePrivilege).fetchJoin()
        .join(rolePrivilege.privilege, privilege).fetchJoin()
        .where(user.email.eq(email))
        .fetchOne();
  }
}
