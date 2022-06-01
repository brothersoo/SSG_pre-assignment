package com.ssg.shoppingcart.domain;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Group {

  @Id
  @Column(name="group_id")
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(name="name")
  private String name;

  @OneToMany(cascade=CascadeType.ALL)
  @JoinColumn(name="group_id")
  private Collection<Product> products;

  @Builder
  public Group(String name, Collection<Product> products) {
    this.name = name;
    this.products = products;
  }
}
