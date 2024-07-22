package com.kwy.cafebom.front.cart.domain;


import com.kwy.cafebom.common.BaseTimeEntity;
import com.kwy.cafebom.common.type.CartOrderStatus;
import com.kwy.cafebom.front.member.domain.Member;
import com.kwy.cafebom.front.product.domain.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private Integer quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CartOrderStatus status;

    public void addQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public void subtractQuantity(Integer quantity) {
        this.quantity -= quantity;
    }

    public static Cart createCart(Member member, Product product, int quantity,
        CartOrderStatus status) {
        return Cart.builder()
            .member(member)
            .product(product)
            .quantity(quantity)
            .status(status)
            .build();
    }
}