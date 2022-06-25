package io.brothersoo.ecommerce.service.product;

import io.brothersoo.ecommerce.repository.product.ProductRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  ProductRepository productRepository;
  @InjectMocks
  ProductServiceImpl productService;
}
