package com.scheduler.orderservice.product.dto;

import com.scheduler.orderservice.order.common.dto.OrderRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProductDto {

    @Getter
    @Setter
    public static class ProductInfo {

        private String productName;

        private Integer price;

        private Integer quantity;

    }

}
