package com.kwy.cafebom.front.product.dto;

import com.kwy.cafebom.common.type.SoldOutStatus;
import com.kwy.cafebom.front.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BestProductDto {

    private Integer productId;

    private String name;

    private Integer price;

    private SoldOutStatus soldOutStatus;

    private String picture;

    public static BestProductDto from(Product product) {
        return BestProductDto.builder()
            .productId(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .soldOutStatus(product.getSoldOutStatus())
            .picture(product.getPicture())
            .build();
    }
}
