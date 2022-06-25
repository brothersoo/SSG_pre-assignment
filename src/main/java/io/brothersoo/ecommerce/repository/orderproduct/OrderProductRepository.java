package io.brothersoo.ecommerce.repository.orderproduct;

import io.brothersoo.ecommerce.domain.product.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
