package com.ssg.shoppingcart.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssg.shoppingcart.domain.BaseTimeStampEntity;
import com.ssg.shoppingcart.domain.product.OrderProduct;
import com.ssg.shoppingcart.domain.user.User;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 엔티티
 */
@Entity
@Table(name = "ssg_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseTimeStampEntity {

  @Id
  @Column(name = "ssg_order_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "status")
  @Enumerated(value = EnumType.STRING)
  private OrderStatus status;

  @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "ssg_user_id")
  @JsonBackReference
  private User user;

  @OneToMany(targetEntity = OrderProduct.class, mappedBy = "order", fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<OrderProduct> orderProducts;

  @Builder
  public Order(User user, OrderStatus status, Set<OrderProduct> orderProducts) {
    this.user = user;
    this.status = status;
    this.orderProducts = orderProducts;
  }

  public void setOrderProducts(Set<OrderProduct> orderProducts) {
    this.orderProducts = orderProducts;
  }

  public void changeStatus(OrderStatus status) {
    this.status = status;
  }
}
