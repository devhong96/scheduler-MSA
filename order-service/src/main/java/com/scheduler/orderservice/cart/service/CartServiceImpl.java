package com.scheduler.orderservice.cart.service;

import com.scheduler.orderservice.cart.domain.Cart;
import com.scheduler.orderservice.cart.dto.CartResponse;
import com.scheduler.orderservice.cart.repository.CartJpaRepository;
import com.scheduler.orderservice.order.client.MemberServiceClient;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.scheduler.orderservice.cart.dto.CartRequest.*;
import static com.scheduler.orderservice.order.client.dto.MemberFeignDto.StudentResponse;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartJpaRepository cartJpaRepository;
    private final MemberServiceClient memberServiceClient;

    @Override
    @Transactional
    public CartResponse createCart(String username, CartCreateRequest createRequest) {
        StudentResponse studentInfo = memberServiceClient.findStudentByUsername(username);

        return null;
    }

    @Override
    @Transactional
    public CartResponse updateCart(String username, UpdateQuantityRequest quantityRequest) {
        StudentResponse studentInfo = memberServiceClient.findStudentByUsername(username);

        Cart cart = cartJpaRepository.findCartByCartIdAndStudentId(quantityRequest.getCartId(), studentInfo.getStudentId())
                .orElseThrow(EntityExistsException::new);

        cart.updateCount(quantityRequest.getQuantity());

        return null;
    }

    @Override
    @Transactional
    public CartResponse checkedCart(String username, UpdateCheckedRequest checkedRequest) {
        StudentResponse studentInfo = memberServiceClient.findStudentByUsername(username);

        Cart cart = cartJpaRepository.findCartByCartIdAndStudentId(checkedRequest.getCartId(), studentInfo.getStudentId())
                .orElseThrow(EntityExistsException::new);

        cart.updateChecked(checkedRequest.getChecked());

        return null;
    }

    @Override
    @Transactional
    public Long deleteCart(String username, DeleteCartRequest deleteCartRequest) {
        StudentResponse studentInfo = memberServiceClient.findStudentByUsername(username);

        return cartJpaRepository.deleteCartsByCartIdAndStudentId(
                deleteCartRequest.getCartId(), studentInfo.getStudentId());

    }
}
