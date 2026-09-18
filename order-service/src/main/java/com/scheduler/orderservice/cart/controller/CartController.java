package com.scheduler.orderservice.cart.controller;

import com.scheduler.orderservice.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.scheduler.orderservice.cart.dto.CartRequest.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 추가
    @PostMapping("{orderId}")
    public ResponseEntity<Void> createCart(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestBody CartCreateRequest cartCreateRequest
    ) {
        return new ResponseEntity<>(OK);
    }


    // 수량 변경
    @PatchMapping("{cart}")
    public ResponseEntity<Void> changeCount(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestBody UpdateQuantityRequest updateQuantityRequest
    ) {
        return new ResponseEntity<>(OK);
    }


    // 장바구니 체크
    @PatchMapping("{boolean}")
    public ResponseEntity<Void> checkCart(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestBody UpdateCheckedRequest updateCheckedRequest
    ) {
        return new ResponseEntity<>(OK);
    }


    // 장바구니 삭제
    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> deleteCart(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestBody DeleteCartRequest deleteCartRequest
    ) {
        return new ResponseEntity<>(OK);
    }
}
