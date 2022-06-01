package com.ssg.shoppingcart.repository.product;

import com.ssg.shoppingcart.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends ProductRepositoryCustom, JpaRepository<Product, Long> {

}
