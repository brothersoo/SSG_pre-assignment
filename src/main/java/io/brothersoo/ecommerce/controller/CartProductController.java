package io.brothersoo.ecommerce.controller;

import io.brothersoo.ecommerce.domain.user.User;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductAddRequest;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductInfo;
import io.brothersoo.ecommerce.dto.CartProductDto.CartProductQuantityModifyRequest;
import io.brothersoo.ecommerce.dto.CartProductDto.QuantityResetRequest;
import io.brothersoo.ecommerce.service.auth.AuthService;
import io.brothersoo.ecommerce.service.cartproduct.CartProductService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 장바구니 관련 기능 controller
 */
@RestController
@RequestMapping("/cart_product")
@RequiredArgsConstructor
public class CartProductController {

  private final CartProductService cartProductService;
  private final AuthService authService;

  /**
   * 선택한 상품을 지정한 수량만큼 장바구니에 담습니다.
   *
   * @param productId   장바구니에 담을 상품 id
   * @param request     header에 담길 token을 위한 http request
   * @param requestBody 추가 수량 데이터가 담긴 DTO
   * @return 장바구니에 담긴 장바구니 상품 데이터를 반환합니다.
   */
  @PostMapping("/{productId}")
  public ResponseEntity<CartProductInfo> cartProductAdd(
      @PathVariable("productId") Long productId,
      HttpServletRequest request,
      @RequestBody CartProductAddRequest requestBody
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.addProductToCart(
            user,
            productId,
            requestBody.getQuantity()
        ),
        HttpStatus.CREATED
    );
  }

  /**
   * 사용자의 장바구니에 담긴 상품들을 불러옵니다.
   *
   * @param request header에 담길 token을 위한 http request
   * @return 그룹을 key로, 해당 그룹에 포함되어있는 상품의 정보 리스트를 value로 가지는 HashMap을 반환합니다.
   */
  @GetMapping
  public ResponseEntity<Map<String, List<CartProductInfo>>> cartProductRetrieve(
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.findAllCartProductsForUser(user), HttpStatus.OK);
  }

  /**
   * 장바구니에 담긴 상품의 수량을 변경합니다.
   *
   * @param cartProductId 수량을 변경할 장바구니 상품의 id
   * @param request       header에 담길 token을 위한 http request
   * @param requestBody   변경 수량 데이터가 담긴 DTO
   * @return 수량이 변경된 장바구니 상품 데이터를 반환합니다.
   */
  @PutMapping("/{cartProductId}")
  public ResponseEntity<CartProductInfo> cartProductQuantityModify(
      @PathVariable("cartProductId") Long cartProductId,
      HttpServletRequest request,
      @RequestBody CartProductQuantityModifyRequest requestBody
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.modifyCartProductQuantity(
            cartProductId, user, requestBody.getQuantity()
        ),
        HttpStatus.OK
    );
  }

  /**
   * 장바구니에서 상품을 제외시킵니다.
   *
   * @param cartProductId 제외시킬 장바구니 상품의 id
   * @param request       header에 담길 token을 위한 http request
   * @return 제외시킨 장바구니 상품의 id를 반환합니다.
   */
  @DeleteMapping("/{cartProductId}")
  public ResponseEntity<Long> cartProductDelete(
      @PathVariable("cartProductId") Long cartProductId,
      HttpServletRequest request
  ) {
    User user = authService.findUserByHttpRequest(request);

    return new ResponseEntity<>(
        cartProductService.deleteCartProductById(cartProductId, user),
        HttpStatus.OK
    );
  }

  /**
   * 장바구니 상품 수량이 해당 상품의 재고를 초과한 장바구니 상품들을 처리합니다.
   * type이 reset인 경우 장바구니 상품의 수량을 해당 상품의 재고로 변경합니다.
   * type이 remove인 경우 장바구니 상품을 장바구니에서 제거합니다.
   *
   * @param request              header에 담길 token을 위한 http request
   * @param quantityResetRequest "reset" 아니면 "remove"
   * @return
   */
  @PutMapping("/reset_in_stock")
  public ResponseEntity<List<Long>> cartProductQuantityExceedingStockHandle(
      HttpServletRequest request,
      @RequestBody QuantityResetRequest quantityResetRequest
  ) {
    User user = authService.findUserByHttpRequest(request);

    List<Long> cartProductIds
        = cartProductService.handleCartProductQuantityExceededStock(
        quantityResetRequest.getType(),
        user
    );
    return new ResponseEntity<>(cartProductIds, HttpStatus.OK);
  }
}
