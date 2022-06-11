package com.ssg.shoppingcart.domain.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssg.shoppingcart.domain.BaseTimeStampEntity;
import java.util.Set;
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
@Table(name = "ssg_product_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductGroup extends BaseTimeStampEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ssg_product_group_id")
  private Long id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @OneToMany(targetEntity = Product.class, cascade = CascadeType.ALL, mappedBy = "productGroup")
  @JsonManagedReference
  private Set<Product> products;

  @Builder
  public ProductGroup(String name, Set<Product> products) {
    this.name = name;
    this.products = products;
  }
}
