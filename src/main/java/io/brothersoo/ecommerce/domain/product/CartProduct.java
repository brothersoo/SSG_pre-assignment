package io.brothersoo.ecommerce.domain.product;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 장바구니에 담긴 상품 엔티티
 */
@Entity
@Table(name = "ecommerce_cart_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartProduct extends BaseTimeStampEntity {

  @Id
  @Column(name = "ecommerce_cart_product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @OneToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_product_id")
  private Product product;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_user_id")
  @JsonBackReference
  private User user;

  @Builder
  public CartProduct(Integer quantity, Product product, User user) {
    this.quantity = quantity;
    this.product = product;
    this.user = user;
  }

  public void modifyQuantity(int quantity) {
    this.quantity = quantity;
  }

  public boolean isOrderableQuantity() {
    return quantity > product.getStock();
  }

  public void orderQuantity() {
    this.getProduct().subtractStock(quantity);
  }

  public boolean isInStock() {
    return product.isInStock();
  }
}
