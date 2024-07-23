package com.kwy.cafebom.front.service.impl;

import static com.kwy.cafebom.common.exception.ErrorCode.CARTOPTION_NOT_EXISTS;
import static com.kwy.cafebom.common.exception.ErrorCode.CART_DOES_NOT_EXIST;
import static com.kwy.cafebom.common.exception.ErrorCode.MEMBER_NOT_EXISTS;
import static com.kwy.cafebom.common.exception.ErrorCode.OPTION_NOT_EXISTS;
import static com.kwy.cafebom.common.exception.ErrorCode.PRODUCT_NOT_EXISTS;
import static com.kwy.cafebom.common.type.CartOrderStatus.BEFORE_ORDER;

import com.kwy.cafebom.common.config.security.TokenProvider;
import com.kwy.cafebom.common.exception.CustomException;
import com.kwy.cafebom.front.cart.domain.Cart;
import com.kwy.cafebom.front.cart.domain.CartOption;
import com.kwy.cafebom.front.cart.domain.CartOptionRepository;
import com.kwy.cafebom.front.cart.domain.CartRepository;
import com.kwy.cafebom.front.cart.dto.CartAddForm;
import com.kwy.cafebom.front.cart.dto.CartListDto;
import com.kwy.cafebom.front.cart.dto.CartModifyForm;
import com.kwy.cafebom.front.cart.dto.CartRemoveForm;
import com.kwy.cafebom.front.cart.dto.OptionListForm;
import com.kwy.cafebom.front.member.domain.Member;
import com.kwy.cafebom.front.member.domain.MemberRepository;
import com.kwy.cafebom.front.product.domain.Option;
import com.kwy.cafebom.front.product.domain.OptionCategoryRepository;
import com.kwy.cafebom.front.product.domain.OptionRepository;
import com.kwy.cafebom.front.product.domain.Product;
import com.kwy.cafebom.front.product.domain.ProductOptionCategoryRepository;
import com.kwy.cafebom.front.product.domain.ProductRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final MemberRepository memberRepository;

    private final CartRepository cartRepository;

    private final TokenProvider tokenProvider;

    private final ProductRepository productRepository;

    private final ProductOptionCategoryRepository productOptionCategoryRepository;

    private final CartOptionRepository cartOptionRepository;

    private final OptionRepository optionRepository;

    private final OptionCategoryRepository optionCategoryRepository;

    @Transactional(readOnly = true)
    public List<CartListDto> findCartList(String token) {

        Member member = memberRepository.findById(tokenProvider.getId(token))
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findAllByMember(member);

        List<CartListDto> cartListDtoList = new ArrayList<>();

        for (Cart cart : cartList) {
            List<CartOption> cartOptionList = cartOptionRepository.findAllByCart(cart);

            List<OptionListForm> optionListFormList = new ArrayList<>();

            for (CartOption cartOption : cartOptionList) {
                optionListFormList.add(OptionListForm.from(cartOption));
            }

            cartListDtoList.add(CartListDto.from(cart, optionListFormList));
        }

        return cartListDtoList;
    }

    public void saveCart(String token, CartAddForm cartAddForm) {

        Member member = memberRepository.findById(tokenProvider.getId(token))
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Product product = productRepository.findById(cartAddForm.getProductId())
            .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<Integer> optionIdList = cartAddForm.getOptionIdList();

        Collections.sort(optionIdList);

        Cart cart = Cart.createCart(member, product, cartAddForm.getQuantity(), BEFORE_ORDER);

        List<Cart> cartList = cartRepository.findAllByMemberAndProduct(member, product);

        for (Cart otherCart : cartList) {

            List<Integer> otherOptionIdList = extractOptionIdList(cart);

            if (isSameOptionIdList(optionIdList, otherOptionIdList) && otherCart.getStatus() == BEFORE_ORDER) {
                otherCart.addQuantity(cart.getQuantity());
                return;
            }
        }

        for (Integer optionId : optionIdList) {
            Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(OPTION_NOT_EXISTS));

            cartOptionRepository.save(CartOption.createCartOption(cart, option));
        }
    }

    public void removeCart(String token, CartRemoveForm cartRemoveForm) {

        Member member = memberRepository.findById(tokenProvider.getId(token))
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Product product = productRepository.findById(cartRemoveForm.getProductId())
            .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findAllByMemberAndProduct(member, product);

        List<Integer> removeOptionIdList = cartRemoveForm.getOptionIdList();

        Collections.sort(removeOptionIdList);

        for (Cart cart : cartList) {

            List<Integer> optionIdList = new ArrayList<>();

            List<CartOption> cartOptionList = cartOptionRepository.findAllByCart(cart);

            for (CartOption cartOption : cartOptionList) {
                optionIdList.add(cartOption.getOption().getId());
            }

            if (isSameOptionIdList(removeOptionIdList, optionIdList)) {
                cartRepository.delete(cart);
                cartOptionRepository.deleteAllByCart(cart);
                return;
            }
            throw new CustomException(CARTOPTION_NOT_EXISTS);
        }
    }

    public void increaseCart(String token, CartModifyForm cartModifyForm) {

        Member member = memberRepository.findById(tokenProvider.getId(token))
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Product product = productRepository.findById(cartModifyForm.getProductId())
            .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findAllByMemberAndProduct(member, product);

        List<Integer> increaseOptionIdList = cartModifyForm.getOptionIdList();

        for (Cart cart : cartList) {

            List<Integer> optionIdList = new ArrayList<>();

            List<CartOption> cartOptionList = cartOptionRepository.findAllByCart(cart);

            for (CartOption cartOption : cartOptionList) {
                optionIdList.add(cartOption.getOption().getId());
            }

            if (isSameOptionIdList(increaseOptionIdList, optionIdList)) {
                cart.addQuantity(1);
                return;
            }
            throw new CustomException(CARTOPTION_NOT_EXISTS);
        }
    }

    public void decreaseCart(String token, CartModifyForm cartModifyForm) {

        Member member = memberRepository.findById(tokenProvider.getId(token))
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Product product = productRepository.findById(cartModifyForm.getProductId())
            .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<Cart> cartList = cartRepository.findAllByMemberAndProduct(member, product);

        List<Integer> increaseOptionIdList = cartModifyForm.getOptionIdList();

        for (Cart cart : cartList) {

            List<Integer> optionIdList = new ArrayList<>();

            List<CartOption> cartOptionList = cartOptionRepository.findAllByCart(cart);

            for (CartOption cartOption : cartOptionList) {
                optionIdList.add(cartOption.getOption().getId());
            }

            if (isSameOptionIdList(increaseOptionIdList, optionIdList)) {
                if (cart.getQuantity() == 1) {
                    cartRepository.delete(cart);
                } else {
                    cart.subtractQuantity(1);
                }
                return;
            }
            throw new CustomException(CARTOPTION_NOT_EXISTS);
        }
    }

    private List<Integer> extractOptionIdList(Cart cart) {

        List<CartOption> cartOptionList = cartOptionRepository.findAllByCart(cart);

        List<Integer> optionIdList = new ArrayList<>();

        for (CartOption cartOption : cartOptionList) {
            optionIdList.add(cartOption.getOption().getId());
        }

        return optionIdList;
    }

    private boolean isSameOptionIdList(List<Integer> optionIdList1, List<Integer> optionIdList2) {
        return optionIdList1.equals(optionIdList2);
    }
}