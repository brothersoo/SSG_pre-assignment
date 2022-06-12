package com.ssg.shoppingcart.domain.auth;

import com.ssg.shoppingcart.domain.BaseTimeStampEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 어떠한 기능에 대한 권한을 알리는 엔티티
 * Role 내에 여러 Privilege가 포함되어있습니다.
 */
@Entity
@Table(name = "ssg_privilege")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Privilege extends BaseTimeStampEntity {

  @Id
  @Column(name = "ssg_privilege_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Builder
  public Privilege(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
