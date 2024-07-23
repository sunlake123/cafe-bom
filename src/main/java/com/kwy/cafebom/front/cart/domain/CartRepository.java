package com.kwy.cafebom.front.cart.domain;

import com.kwy.cafebom.common.type.CartOrderStatus;
import com.kwy.cafebom.front.member.domain.Member;
import com.kwy.cafebom.front.product.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByMember(Member member);

    List<Cart> findAllByMemberAndStatus(Member member, CartOrderStatus status);

    void deleteAllByMember(Member member);

    List<Cart> findAllByMemberAndProduct(Member member , Product product);

    List<Cart> findByMember(Member member);
}
