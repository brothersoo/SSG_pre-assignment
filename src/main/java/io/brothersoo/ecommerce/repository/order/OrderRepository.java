package io.brothersoo.ecommerce.repository.order;

import io.brothersoo.ecommerce.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

}
