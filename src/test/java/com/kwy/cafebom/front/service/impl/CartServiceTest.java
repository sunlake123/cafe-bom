package com.kwy.cafebom.front.service.impl;

import static com.kwy.cafebom.common.config.security.Role.ROLE_USER;
import static com.kwy.cafebom.common.type.CartOrderStatus.BEFORE_ORDER;
import static com.kwy.cafebom.common.type.SoldOutStatus.IN_STOCK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.kwy.cafebom.common.config.security.TokenProvider;
import com.kwy.cafebom.front.cart.domain.Cart;
import com.kwy.cafebom.front.cart.domain.CartOption;
import com.kwy.cafebom.front.cart.domain.CartOptionRepository;
import com.kwy.cafebom.front.cart.domain.CartRepository;
import com.kwy.cafebom.front.cart.dto.CartAddForm;
import com.kwy.cafebom.front.cart.dto.CartListDto;
import com.kwy.cafebom.front.cart.dto.CartListForm;
import com.kwy.cafebom.front.cart.dto.CartRemoveForm;
import com.kwy.cafebom.front.cart.dto.OptionListForm;
import com.kwy.cafebom.front.member.domain.Member;
import com.kwy.cafebom.front.member.domain.MemberRepository;
import com.kwy.cafebom.front.product.domain.Option;
import com.kwy.cafebom.front.product.domain.OptionCategory;
import com.kwy.cafebom.front.product.domain.OptionCategoryRepository;
import com.kwy.cafebom.front.product.domain.OptionRepository;
import com.kwy.cafebom.front.product.domain.Product;
import com.kwy.cafebom.front.product.domain.ProductCategory;
import com.kwy.cafebom.front.product.domain.ProductOptionCategoryRepository;
import com.kwy.cafebom.front.product.domain.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOptionCategoryRepository productOptionCategoryRepository;

    @Mock
    private CartOptionRepository cartOptionRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OptionCategoryRepository optionCategoryRepository;

    @InjectMocks
    private CartService cartService;

    static String TOKEN = "bearer token";

    Member member = Member.builder()
        .password("password")
        .nickname("nickname")
        .phone("010-1234-5678")
        .email("abcde@fghij.com")
        .role(ROLE_USER)
        .build();

    ProductCategory coffee = ProductCategory.builder()
        .id(1)
        .name("커피")
        .build();

    Product espresso = Product.builder()
        .id(1)
        .productCategory(coffee)
        .name("에스프레소")
        .description("씁쓸한 에스프레소")
        .price(1500)
        .soldOutStatus(IN_STOCK)
        .picture("pictureEspresso")
        .build();

    OptionCategory iceAmount = OptionCategory.builder()
        .id(1)
        .name("얼음 양")
        .build();

    OptionCategory size = OptionCategory.builder()
        .id(2)
        .name("사이즈")
        .build();

    Option iceAmount1 = Option.builder()
        .id(1)
        .optionCategory(iceAmount)
        .price(300)
        .build();

    Option size1 = Option.builder()
        .id(3)
        .optionCategory(size)
        .price(500)
        .build();


    @BeforeEach
    private void beforeEach() {
        given(tokenProvider.getId(TOKEN)).willReturn(1L);
    }

    @Test
    @DisplayName("장바구니 등록 성공 - 장바구니가 비어있을 때")
    void successSaveCartEmptyCart() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(productRepository.findById(1)).willReturn(Optional.of(espresso));

        List<Integer> optionIdList = new ArrayList<>();

        optionIdList.add(1);
        optionIdList.add(3);

        CartAddForm cartAddForm = CartAddForm.builder()
            .productId(1)
            .quantity(5)
            .optionIdList(optionIdList)
            .cartOrderStatus(BEFORE_ORDER)
            .build();

        given(optionRepository.findById(1)).willReturn(Optional.of(iceAmount1));
        given(optionRepository.findById(3)).willReturn(Optional.of(size1));

        given(cartRepository.findAllByMemberAndProduct(member, espresso)).willReturn(
            new ArrayList<>());

        // when
        cartService.saveCart(TOKEN, cartAddForm);

        // then
    }

    @Test
    @DisplayName("장바구니 조회 성공")
    void successFindCartList() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        List<Cart> cartList = new ArrayList<>();

        Cart cart = Cart.createCart(member, espresso, 5, BEFORE_ORDER);

        cartList.add(cart);

        given(cartRepository.findAllByMember(member)).willReturn(cartList);

        List<CartOption> cartOptionList = new ArrayList<>();

        cartOptionList.add(CartOption.createCartOption(cart, iceAmount1));
        cartOptionList.add(CartOption.createCartOption(cart, size1));

        given(cartOptionRepository.findAllByCart(cart)).willReturn(cartOptionList);

        List<OptionListForm> optionListFormList = new ArrayList<>();
        for (CartOption cartOption : cartOptionList) {
            optionListFormList.add(OptionListForm.from(cartOption));
        }

        // when
        List<CartListDto> cartListDtoList = cartService.findCartList(TOKEN);

        // then
        assertThat(cartListDtoList.get(0).getProductId()).isEqualTo(1);
        assertThat(cartListDtoList.get(0).getProductName()).isEqualTo(espresso.getName());
        assertThat(cartListDtoList.get(0).getProductPicture()).isEqualTo(espresso.getPicture());
        assertThat(cartListDtoList.get(0).getProductPrice()).isEqualTo(espresso.getPrice());
        assertThat(cartListDtoList.get(0).getProductQuantity()).isEqualTo(cart.getQuantity());
        assertThat(cartListDtoList.get(0).getOptionListFormList()).isEqualTo(optionListFormList);
        assertThat(cartListDtoList.get(0).getTotalPrice()).isEqualTo(CartListDto.totalPrice(espresso, optionListFormList));
        assertThat(cartListDtoList.get(0).getCartOrderStatus()).isEqualTo(cart.getStatus());
    }

    @Test
    @DisplayName("장바구니 삭제 성공")
    void successRemoveCart() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(productRepository.findById(1)).willReturn(Optional.of(espresso));

        List<Cart> cartList = new ArrayList<>();

        Cart cart = Cart.createCart(member, espresso, 5, BEFORE_ORDER);

        cartList.add(cart);

        given(cartRepository.findAllByMemberAndProduct(member, espresso))
            .willReturn(cartList);

        List<CartOption> cartOptionList = new ArrayList<>();

        cartOptionList.add(CartOption.createCartOption(cart, iceAmount1));
        cartOptionList.add(CartOption.createCartOption(cart, size1));

        given(cartOptionRepository.findAllByCart(cart)).willReturn(cartOptionList);

        List<Integer> optionIdList = new ArrayList<>();

        optionIdList.add(1);
        optionIdList.add(3);

        CartRemoveForm cartRemoveForm = CartRemoveForm.builder()
            .productId(1)
            .optionIdList(optionIdList)
            .build();

        // when
        cartService.removeCart(TOKEN, cartRemoveForm);

        // then
    }

}