package com.ssg.shoppingcart.repository.product;

import com.ssg.shoppingcart.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends ProductRepositoryCustom, JpaRepository<Product, Long> {

}
