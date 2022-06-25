package io.brothersoo.ecommerce.repository.cartproduct;

import io.brothersoo.ecommerce.domain.product.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository
    extends JpaRepository<CartProduct, Long>, CartProductRepositoryCustom {

}
