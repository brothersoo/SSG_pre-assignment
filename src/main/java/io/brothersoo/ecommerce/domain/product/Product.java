package io.brothersoo.ecommerce.domain.product;

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
 * 상품 엔티티
 */
@Entity
@Table(name = "ecommerce_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseTimeStampEntity {

  @Id
  @Column(name = "ecommerce_product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "stock", nullable = false)
  private Integer stock;

  @ManyToOne(targetEntity = ProductGroup.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_product_group_id")
  @JsonBackReference
  private ProductGroup productGroup;

  @Builder
  public Product(Long id, String name, Integer price, Integer stock, ProductGroup productGroup) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.productGroup = productGroup;
  }

  public void addStock(Integer quantity) {
    this.stock += quantity;
  }

  public void subtractStock(Integer quantity) {
    this.stock -= quantity;
  }

  public boolean isInStock() {
    return stock > 0;
  }
}
