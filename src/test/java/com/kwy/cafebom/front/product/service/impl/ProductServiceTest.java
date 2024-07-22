package com.kwy.cafebom.front.product.service.impl;

import static com.kwy.cafebom.common.exception.ErrorCode.PRODUCTCATEGORY_NOT_EXISTS;
import static com.kwy.cafebom.common.type.SoldOutStatus.IN_STOCK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.kwy.cafebom.common.exception.CustomException;
import com.kwy.cafebom.front.cart.domain.CartOptionRepository;
import com.kwy.cafebom.front.product.domain.Option;
import com.kwy.cafebom.front.product.domain.OptionCategory;
import com.kwy.cafebom.front.product.domain.OptionCategoryRepository;
import com.kwy.cafebom.front.product.domain.OptionRepository;
import com.kwy.cafebom.front.product.domain.Product;
import com.kwy.cafebom.front.product.domain.ProductCategory;
import com.kwy.cafebom.front.product.domain.ProductCategoryRepository;
import com.kwy.cafebom.front.product.domain.ProductOptionCategory;
import com.kwy.cafebom.front.product.domain.ProductOptionCategoryRepository;
import com.kwy.cafebom.front.product.domain.ProductRepository;
import com.kwy.cafebom.front.product.dto.ProductDetailDto;
import com.kwy.cafebom.front.product.dto.ProductDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductCategoryRepository productCategoryRepository;

    @Mock
    ProductRepository productRepository;
    
    @Mock
    ProductOptionCategoryRepository productOptionCategoryRepository;

    @Mock
    OptionRepository optionRepository;

    @Test
    @DisplayName("카테고리 별 상품 목록 조회 성공")
    void successFindProductList() {
        // given
        List<Product> productList = new ArrayList<>();

        ProductCategory coffee = ProductCategory.builder()
            .id(1)
            .name("커피")
            .build();

        productList.add(Product.builder()
            .id(1)
            .productCategory(coffee)
            .name("아메리카노")
            .description("시원해요")
            .price(2000)
            .soldOutStatus(IN_STOCK)
            .picture("picture")
            .build());

        given(productCategoryRepository.existsById(coffee.getId())).willReturn(true);
        given(productRepository.findAllByProductCategoryId(coffee.getId())).willReturn(productList);

        // when
        List<ProductDto> productDtoList = productService.findProductList(coffee.getId());

        // then
        assertThat(productDtoList.get(0).getProductId()).isEqualTo(productList.get(0).getId());
        assertThat(productDtoList.get(0).getName()).isEqualTo(productList.get(0).getName());
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(productList.get(0).getPrice());
        assertThat(productDtoList.get(0).getSoldOutStatus()).isEqualTo(productList.get(0).getSoldOutStatus());
        assertThat(productDtoList.get(0).getPicture()).isEqualTo(productList.get(0).getPicture());
    }

    @Test
    @DisplayName("카테고리 별 상품 목록 조회 실패 - 존재하지 않는 상품 카테고리")
    void failFindProductListProductCategoryNotFound() {
        // when
        assertThatThrownBy(() -> productService.findProductList(1))
            .isExactlyInstanceOf(CustomException.class)
            .hasMessage(PRODUCTCATEGORY_NOT_EXISTS.getMessage());
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void successFindProductDetails() {
        // given
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
            .picture("picture")
            .build();

        OptionCategory size = OptionCategory.builder()
            .id(1)
            .name("사이즈")
            .build();

        ProductOptionCategory espressoSize = ProductOptionCategory.builder()
            .product(espresso)
            .optionCategory(size)
            .build();

        given(productRepository.findById(espresso.getId())).willReturn(Optional.of(espresso));

        List<ProductOptionCategory> productOptionCategoryList = new ArrayList<>();
        productOptionCategoryList.add(espressoSize);

        given(productOptionCategoryRepository.findAllByProduct(espresso)).willReturn(productOptionCategoryList);

        List<Option> optionList = new ArrayList<>();

        Option option = Option.builder()
            .optionCategory(size)
            .name("톨 사이즈")
            .price(500)
            .build();

        optionList.add(option);

        given(optionRepository.findAllByOptionCategory(
            productOptionCategoryList.get(0).getOptionCategory())).willReturn(optionList);

        Map<ProductOptionCategory, List<Option>> map = new HashMap<>();

        map.put(productOptionCategoryList.get(0), optionList);

        // when
        ProductDetailDto productDetails = productService.findProductDetails(espresso.getId());

        // then
        assertThat(productDetails.getProductId()).isEqualTo(espresso.getId());
        assertThat(productDetails.getName()).isEqualTo(espresso.getName());
        assertThat(productDetails.getDescription()).isEqualTo(espresso.getDescription());
        assertThat(productDetails.getPrice()).isEqualTo(espresso.getPrice());
        assertThat(productDetails.getSoldOutStatus()).isEqualTo(espresso.getSoldOutStatus());
        assertThat(productDetails.getPicture()).isEqualTo(espresso.getPicture());
        assertThat(productDetails.getProductOptionList().get(espressoSize)).isEqualTo(optionList);
    }
}











