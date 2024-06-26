package com.kwy.cafebom.front.product.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionCategoryRepository extends
    JpaRepository<ProductOptionCategory, Integer> {

    List<ProductOptionCategory> findAllByProduct(Product product);

}
