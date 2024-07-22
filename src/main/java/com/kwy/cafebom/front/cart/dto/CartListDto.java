package com.kwy.cafebom.front.cart.dto;

import com.kwy.cafebom.common.type.CartOrderStatus;
import com.kwy.cafebom.front.cart.domain.Cart;
import com.kwy.cafebom.front.product.domain.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CartListDto {

    private Integer productId;

    private String productName;

    private String productPicture;

    private int productPrice;

    private int productQuantity;

    private List<OptionListForm> optionListFormList;

    private int totalPrice;

    private CartOrderStatus cartOrderStatus;

    public static CartListDto from(Cart cart, List<OptionListForm> optionListFormList) {
        return CartListDto.builder()
            .productId(cart.getProduct().getId())
            .productName(cart.getProduct().getName())
            .productPicture(cart.getProduct().getPicture())
            .productPrice(cart.getProduct().getPrice())
            .productQuantity(cart.getQuantity())
            .optionListFormList(optionListFormList)
            .totalPrice(totalPrice(cart.getProduct(), optionListFormList))
            .cartOrderStatus(cart.getStatus())
            .build();
    }

    public static int totalPrice(Product product, List<OptionListForm> optionListFormList) {
        int totalOptionPrice = 0;

        for (OptionListForm optionListForm : optionListFormList) {
            totalOptionPrice += optionListForm.getOptionPrice();
        }

        return product.getPrice() + totalOptionPrice;
    }
}
