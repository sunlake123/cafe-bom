package com.kwy.cafebom.front.cart.dto;

import com.kwy.cafebom.common.type.CartOrderStatus;
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
public class CartAddForm {

    @NotNull(message = "productId는 필수입니다.")
    private Integer productId;

    @NotNull(message = "quantity는 필수입니다.")
    private Integer quantity;

    @NotNull(message = "optionIdList는 필수입니다.")
    private List<Integer> optionIdList;

    @NotNull(message = "cartOrderStatus는 필수입니다.")
    private CartOrderStatus cartOrderStatus;
}
