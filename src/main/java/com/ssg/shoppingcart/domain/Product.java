package com.ssg.shoppingcart.domain;

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
@Table(name="product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

  @Id
  @Column(name="product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="name")
  private String name;

  @Column(name="price")
  private int price;

  @Column(name="stock")
  private int stock;

  @ManyToOne(targetEntity=Group.class, fetch=FetchType.LAZY)
  @JoinColumn(name="group_id")
  private Group group;

  @Builder
  public Product(String name, int price, int stock, Group group) {
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.group = group;
  }
}
