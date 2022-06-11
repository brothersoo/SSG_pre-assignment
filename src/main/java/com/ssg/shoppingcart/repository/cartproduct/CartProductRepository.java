package com.ssg.shoppingcart.repository.cartproduct;

import com.ssg.shoppingcart.domain.product.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository
    extends JpaRepository<CartProduct, Long>, CartProductRepositoryCustom {

}
