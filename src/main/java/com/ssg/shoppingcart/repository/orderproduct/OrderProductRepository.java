package com.ssg.shoppingcart.repository.orderproduct;

import com.ssg.shoppingcart.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
