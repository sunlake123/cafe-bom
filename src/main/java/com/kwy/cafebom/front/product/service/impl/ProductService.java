package com.kwy.cafebom.front.product.service.impl;

import static com.kwy.cafebom.common.exception.ErrorCode.PRODUCT_NOT_EXISTS;

import com.kwy.cafebom.common.exception.CustomException;
import com.kwy.cafebom.common.exception.ErrorCode;
import com.kwy.cafebom.front.product.domain.Option;
import com.kwy.cafebom.front.product.domain.OptionRepository;
import com.kwy.cafebom.front.product.domain.Product;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final OptionRepository optionRepository;

    private final ProductOptionCategoryRepository productOptionCategoryRepository;

    @Transactional(readOnly = true)
    public List<ProductDto> findProductList(Integer productCategoryId) {

        if (!productCategoryRepository.existsByProductCategoryId(productCategoryId)) {
            throw new CustomException(ErrorCode.PRODUCTCATEGORY_NOT_EXISTS);
        }

        List<Product> productList = productRepository.findAllByProductCategoryId(productCategoryId);
        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product product : productList) {
            productDtoList.add(ProductDto.from(product));
        }

        return productDtoList;
    }

    public ProductDetailDto findProductDetails(Integer productId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));

        List<ProductOptionCategory> allByProduct =
            productOptionCategoryRepository.findAllByProduct(product);

        Map<ProductOptionCategory, List<Option>> map = new HashMap<>();

        for (ProductOptionCategory productOptionCategory : allByProduct) {

            List<Option> optionList = optionRepository.findAllByOptionCategory(
                productOptionCategory.getOptionCategory());

            map.put(productOptionCategory, optionList);
        }

        return ProductDetailDto.from(product, map);
    }
}
