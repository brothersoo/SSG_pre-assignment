package com.ssg.shoppingcart.domain.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssg.shoppingcart.domain.BaseTimeStampEntity;
import javax.persistence.CascadeType;
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

@Entity
@Table(name = "ssg_role_privilege")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RolePrivilege extends BaseTimeStampEntity {

  @Id
  @Column(name = "ssg_role_privilege_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "ssg_role_id")
  @JsonBackReference
  private Role role;

  @ManyToOne(targetEntity = Privilege.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "ssg_privilege_id")
  @JsonBackReference
  private Privilege privilege;

  @Builder
  public RolePrivilege(Long id, Role role, Privilege privilege) {
    this.id = id;
    this.role = role;
    this.privilege = privilege;
  }
}
