package io.brothersoo.ecommerce.repository.product;

import io.brothersoo.ecommerce.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends ProductRepositoryCustom, JpaRepository<Product, Long> {

}
