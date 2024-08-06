package com.whatever_market.app.product.repository;

import com.whatever_market.app.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    Page<Product> findAllByIdIn(List<Long> ids, Pageable pageable);
}
