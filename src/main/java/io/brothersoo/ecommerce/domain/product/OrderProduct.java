package io.brothersoo.ecommerce.domain.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.brothersoo.ecommerce.domain.BaseTimeStampEntity;
import io.brothersoo.ecommerce.domain.order.Order;
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
 * 주문된 상품 엔티티
 */
@Entity
@Table(name = "ecommerce_order_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderProduct extends BaseTimeStampEntity {

  @Id
  @Column(name = "ecommerce_order_product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_order_id", nullable = false)
  @JsonBackReference
  private Order order;

  @OneToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "ecommerce_product_id")
  private Product product;

  @Builder
  public OrderProduct(Integer price, Integer quantity, Order order, Product product) {
    this.price = price;
    this.quantity = quantity;
    this.order = order;
    this.product = product;
  }

  public void refundQuantity() {
    product.addStock(quantity);
  }
}
