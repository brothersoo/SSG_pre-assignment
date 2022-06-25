package io.brothersoo.ecommerce.domain.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.brothersoo.ecommerce.domain.BaseTimeStampEntity;
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
 * Role과 Privilege 간 Many-to-Many 관계 엔티티
 */
@Entity
@Table(name = "ecommerce_role_privilege")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RolePrivilege extends BaseTimeStampEntity {

  @Id
  @Column(name = "ecommerce_role_privilege_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(targetEntity = Role.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_role_id")
  @JsonBackReference
  private Role role;

  @ManyToOne(targetEntity = Privilege.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_privilege_id")
  @JsonBackReference
  private Privilege privilege;

  @Builder
  public RolePrivilege(Long id, Role role, Privilege privilege) {
    this.id = id;
    this.role = role;
    this.privilege = privilege;
  }
}
