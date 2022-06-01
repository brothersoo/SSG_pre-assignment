package com.ssg.shoppingcart.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Table(name="ssg_order")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
public class Order extends BaseTimeStampEntity {

  @Id
  @Column(name="ssg_order_id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(name="total_price", nullable=false)
  private Integer totalPrice;

  @ManyToOne(targetEntity=User.class, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinColumn(name="ssg_user_id")
  private User user;

  @OneToMany(targetEntity=OrderProduct.class, cascade=CascadeType.ALL, mappedBy="order")
  private List<OrderProduct> orderProducts;

  @Builder
  public Order(Integer totalPrice, User user, List<OrderProduct> orderProducts) {
    this.totalPrice = totalPrice;
    this.user = user;
    this.orderProducts = orderProducts;
  }
}
