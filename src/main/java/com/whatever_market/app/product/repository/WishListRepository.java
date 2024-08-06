package com.whatever_market.app.product.repository;

import com.whatever_market.app.product.model.Product;
import com.whatever_market.app.product.model.WishList;
import com.whatever_market.app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findByUserAndProduct(User user, Product product);
    List<WishList> findAllByUserId(Long userId);
}
