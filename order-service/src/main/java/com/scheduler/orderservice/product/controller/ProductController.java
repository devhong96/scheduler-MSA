package com.scheduler.orderservice.product.controller;

import com.scheduler.orderservice.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody Product product) {
        // TODO: 상품 등록 구현 전 (작성 중이던 코드, 컴파일을 위한 임시 스텁)
        return new ResponseEntity<>(NOT_IMPLEMENTED);
    }
}
