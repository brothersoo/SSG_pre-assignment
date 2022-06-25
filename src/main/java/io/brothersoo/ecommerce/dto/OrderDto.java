package io.brothersoo.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class OrderCartProducts {

    private List<Long> cartProductIds;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class OrderInfo {

    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderProductInfo> orderProducts;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class OrderProductInfo {

    private Long id;
    private String productName;
    private Integer price;
    private Integer quantity;
    private String productGroupName;
  }
}
