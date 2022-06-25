package io.brothersoo.ecommerce.domain.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.brothersoo.ecommerce.domain.BaseTimeStampEntity;
import io.brothersoo.ecommerce.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User과 Role 간 Many-To-Many 관계 엔티티
 */
@Entity
@Table(name = "ecommerce_user_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRole extends BaseTimeStampEntity {

  @Id
  @Column(name = "ecommerce_user_role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_user_id")
  @JsonBackReference
  private User user;

  @ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_role_id")
  @JsonBackReference
  private Role role;

  @Builder
  public UserRole(Long id, User user, Role role) {
    this.id = id;
    this.user = user;
    this.role = role;
  }
}
