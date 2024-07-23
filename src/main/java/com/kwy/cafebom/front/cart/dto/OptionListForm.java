package com.kwy.cafebom.front.cart.dto;

import com.kwy.cafebom.front.cart.domain.CartOption;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class OptionListForm {

    private Integer optionCategoryId;

    private String optionCategoryName;

    private int optionId;

    private String optionName;

    private int optionPrice;

    public static OptionListForm from(CartOption cartOption) {
        return OptionListForm.builder()
            .optionCategoryId(cartOption.getOption().getOptionCategory().getId())
            .optionCategoryName(cartOption.getOption().getOptionCategory().getName())
            .optionId(cartOption.getOption().getId())
            .optionName(cartOption.getOption().getName())
            .optionPrice(cartOption.getOption().getPrice())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OptionListForm that = (OptionListForm) o;
        return getOptionId() == that.getOptionId() && getOptionPrice() == that.getOptionPrice()
            && Objects.equals(getOptionCategoryId(), that.getOptionCategoryId())
            && Objects.equals(getOptionCategoryName(), that.getOptionCategoryName())
            && Objects.equals(getOptionName(), that.getOptionName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOptionCategoryId(), getOptionCategoryName(), getOptionId(),
            getOptionName(), getOptionPrice());
    }
}
