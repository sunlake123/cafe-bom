package com.kwy.cafebom.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 컨트롤러 @PathVariable TypeMismatch
    METHOD_ARGUMENT_TYPE_MISMATCH("메서드 매개변수의 타입이 맞지 않습니다.", BAD_REQUEST),

    // Auth
    MEMBER_NOT_EXISTS("존재하지 않는 회원입니다.", BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다.", BAD_REQUEST),
    NICKNAME_ALREADY_EXISTS("이미 존재하는 닉네임입니다.", BAD_REQUEST),
    PHONE_ALREADY_EXISTS("이미 존재하는 휴대전화번호입니다.", BAD_REQUEST),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.", BAD_REQUEST),
    ADMIN_CODE_NOT_MATCH("관리자 인증코드가 일치하지 않습니다.", BAD_REQUEST),

    // Product
    PRODUCT_NOT_EXISTS("존재하지 않는 상품입니다.", BAD_REQUEST),
    BEST_PRODUCT_NOT_EXISTS("베스트 상품이 존재하지 않습니다", BAD_REQUEST),

    // ProductCategory
    PRODUCTCATEGORY_NOT_EXISTS("존재하지 않는 상품 카테고리입니다.", BAD_REQUEST),
    PRODUCTCATEGORY_ALREADY_EXISTS("이미 존재하는 상품 카테고리입니다.", CONFLICT),

    // Cart
    CART_IS_EMPTY("장바구니에 담긴 상품이 없습니다.", BAD_REQUEST),
    CART_DOES_NOT_EXIST("존재하지 않는 장바구니입니다.",BAD_REQUEST),

    // Option
    OPTION_NOT_EXISTS("존재하지 않는 옵션입니다.", BAD_REQUEST),
    OPTION_CATEGORY_NOT_EXISTS("존재하지 않는 옵션 카테고리입니다.", BAD_REQUEST),
    OPTION_OPTION_CATEGORY_NOT_MATCH("옵션 카테고리와 옵션이 일치하지 않습니다.", BAD_REQUEST),

    // CartOption
    CARTOPTION_NOT_EXISTS("해당 옵션의 장바구니 존재하지 않습니다.", BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
