package com.kwy.cafebom.front.cart.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRemoveForm {

    @NotNull(message = "productId는 필수입니다.")
    private Integer productId;

    private List<Integer> optionIdList;
}
