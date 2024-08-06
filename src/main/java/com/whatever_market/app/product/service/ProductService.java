package com.whatever_market.app.product.service;

import com.whatever_market.app.product.dto.ProductRequestDTO;
import com.whatever_market.app.product.dto.WishProductRequestDto;
import com.whatever_market.app.product.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long id, HttpServletRequest request, HttpServletResponse response);
    Product createProduct(ProductRequestDTO productRequestDTO);
    Product updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void deleteProductById(Long id);
    void addWishedProduct(WishProductRequestDto wishProductRequestDto);
    void deleteWishedProduct(WishProductRequestDto wishProductRequestDto);
    Page<Product> getWishedProducts(Long userId, Pageable pageable);
}
