package com.kwy.cafebom.front.product.controller;

import static org.springframework.http.HttpStatus.OK;

import com.kwy.cafebom.front.product.dto.BestProductDto;
import com.kwy.cafebom.front.product.dto.BestProductForm;
import com.kwy.cafebom.front.product.dto.ProductDetailDto;
import com.kwy.cafebom.front.product.dto.ProductDetailForm;
import com.kwy.cafebom.front.product.dto.ProductListForm;
import com.kwy.cafebom.front.product.dto.ProductListForm.Response;
import com.kwy.cafebom.front.product.dto.ProductDto;
import com.kwy.cafebom.front.product.service.impl.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list/{productCategoryId}")
    public ResponseEntity<ProductListForm.Response> productList(@PathVariable Integer productCategoryId) {

        List<ProductDto> productDtoList = productService.findProductList(productCategoryId);

        ProductListForm.Response response = Response.builder()
            .productDtoList(productDtoList)
            .build();

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductDetailForm.Response> productDetails(@PathVariable Integer productId) {
        ProductDetailDto productDetails = productService.findProductDetails(productId);

        return ResponseEntity.status(OK)
            .body(ProductDetailForm.Response.from(productDetails));
    }
}
