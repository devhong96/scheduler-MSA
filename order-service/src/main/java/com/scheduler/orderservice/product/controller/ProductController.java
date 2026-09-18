package com.scheduler.orderservice.product.controller;

import com.scheduler.orderservice.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody Product product){}
}
