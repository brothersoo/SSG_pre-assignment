package com.ssg.shoppingcart.domain.auth;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssg.shoppingcart.domain.BaseTimeStampEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ssg_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Role extends BaseTimeStampEntity {

  @Id
  @Column(name = "ssg_role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToMany(targetEntity = RolePrivilege.class, cascade = CascadeType.ALL, mappedBy = "role")
  @JsonManagedReference
  private List<RolePrivilege> rolePrivileges;

  @Builder
  public Role(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
