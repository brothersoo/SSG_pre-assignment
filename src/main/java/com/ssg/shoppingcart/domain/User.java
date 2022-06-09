package com.ssg.shoppingcart.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssg.shoppingcart.domain.auth.UserRole;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ssg_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeStampEntity {

  @Id
  @Column(name = "ssg_user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "type", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserType type;

  @OneToMany(targetEntity = CartProduct.class, cascade = CascadeType.ALL, mappedBy = "user")
  @JsonManagedReference
  private List<CartProduct> cartProducts;

  @OneToMany(targetEntity = Order.class, cascade = CascadeType.ALL, mappedBy = "user")
  @JsonManagedReference
  private List<Order> orders;

  @OneToMany(targetEntity = UserRole.class, cascade = CascadeType.ALL, mappedBy = "user")
  @JsonManagedReference
  private List<UserRole> userRoles;

  @Builder
  public User(Long id, String email, String password, String username, UserType type,
      List<CartProduct> cartProducts, List<Order> orders, List<UserRole> userRoles) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.username = username;
    this.type = type;
    this.cartProducts = cartProducts;
    this.orders = orders;
    this.userRoles = userRoles;
  }

  public List<String> getAllRoleNames() {
    return this.userRoles.stream().map(userRole -> userRole.getRole().getName())
        .collect(Collectors.toList());
  }

  public List<String> getAllPrivilegeNames() {
    return this.userRoles.stream()
        .flatMap(userRole -> userRole.getRole().getRolePrivileges().stream())
        .map(rolePrivilege -> rolePrivilege.getPrivilege().getName())
        .collect(Collectors.toList());
  }

  @PrePersist
  public void setDefaultType() {
    if (type == null) {
      type = UserType.MEMBER;
    }
  }
}
