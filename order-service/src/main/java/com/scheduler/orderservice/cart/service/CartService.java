package com.scheduler.orderservice.cart.service;

import com.scheduler.orderservice.cart.dto.CartResponse;

import static com.scheduler.orderservice.cart.dto.CartRequest.*;

public interface CartService {

    CartResponse createCart(String username, CartCreateRequest createRequest);

    CartResponse updateCart(String username, UpdateQuantityRequest quantityRequest);

    CartResponse checkedCart(String username, UpdateCheckedRequest checkedRequest);

    Long deleteCart(String username, DeleteCartRequest deleteCartRequest);

}
