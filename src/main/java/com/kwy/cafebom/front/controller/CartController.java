package com.kwy.cafebom.front.controller;

import static org.springframework.http.HttpStatus.OK;

import com.kwy.cafebom.front.cart.dto.CartAddForm;
import com.kwy.cafebom.front.cart.dto.CartListDto;
import com.kwy.cafebom.front.cart.dto.CartListForm;
import com.kwy.cafebom.front.cart.dto.CartModifyForm;
import com.kwy.cafebom.front.cart.dto.CartRemoveForm;
import com.kwy.cafebom.front.service.impl.CartService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartListForm.Response> cartList(
        @RequestHeader(name = "Authorization") String token) {

        List<CartListDto> cartListDtos = cartService.findCartList(token);

        CartListForm.Response response = CartListForm.Response.builder()
            .cartListDtoList(cartListDtos)
            .build();

        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Void> cartSave(
        @RequestBody @Valid CartAddForm cartAddForm,
        @RequestHeader(name = "Authorization") String token) {

        cartService.saveCart(token, cartAddForm);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> cartIncrease(
        @Valid @RequestBody CartModifyForm cartModifyForm,
        @RequestHeader(name = "Authorization") String token) {

        cartService.increaseCart(token, cartModifyForm);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> cartDecrease(
        @Valid @RequestBody CartModifyForm cartModifyForm,
        @RequestHeader(name = "Authorization") String token) {

        cartService.decreaseCart(token, cartModifyForm);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cartRemove(
        @RequestBody @Valid CartRemoveForm cartRemoveForm,
        @RequestHeader(name = "Authorization") String token) {

        cartService.removeCart(token, cartRemoveForm);

        return ResponseEntity.ok().build();
    }

}
