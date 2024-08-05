package com.whatever_market.app.product.service.implement;

import com.whatever_market.app.product.dto.ProductRequestDTO;
import com.whatever_market.app.product.dto.WishProductRequestDto;
import com.whatever_market.app.product.model.WishList;
import com.whatever_market.app.product.repository.ProductRepository;
import com.whatever_market.app.product.model.Product;
import com.whatever_market.app.product.repository.WishListRepository;
import com.whatever_market.app.product.service.ProductService;
import com.whatever_market.app.bible.exception.NotFoundException;
import com.whatever_market.app.user.model.User;
import com.whatever_market.app.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();

        product.setName(productRequestDTO.getName());
        product.setImageURL(productRequestDTO.getImageURL());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setName(productRequestDTO.getName());
        product.setImageURL(productRequestDTO.getImageURL());
        product.setPrice(productRequestDTO.getPrice());
        product.setDescription(productRequestDTO.getDescription());

        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
    }

    // API : 찜하기 요청
    // wishList 테이블에 user PK, Product PK를 활용한 복합 유니크 컬럼 생성, 이를 활용하여 찜하기 요청 중복 처리 방지
    @Override
    public void addWishedProduct(WishProductRequestDto wishProductRequestDto) {
        User user = userRepository.findById(wishProductRequestDto.getUser_id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Product product = productRepository.findById(wishProductRequestDto.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setProduct(product);

        try{
            wishListRepository.save(wishList);
        } catch (DataIntegrityViolationException e){
            log.warn("Duplicate like request occurred.");
        }
    }

    // API : 찜하기 취소
    @Override
    public void deleteWishedProduct(WishProductRequestDto wishProductRequestDto) {
        User user = userRepository.findById(wishProductRequestDto.getUser_id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Product product = productRepository.findById(wishProductRequestDto.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        WishList wishList = wishListRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new NotFoundException("WishList not found"));
        wishListRepository.delete(wishList);
    }

    // API : 찜한 상품 목록들 조회
    @Override
    public Page<Product> getWishedProducts(Long userId, Pageable pageable) {
        List<WishList> wishListLists = wishListRepository.findAllByUserId(userId);
        List<Long> productIds = wishListLists.stream()
                .map(wishList -> wishList.getProduct().getId())
                .collect(Collectors.toList());
        return productRepository.findAllByIdIn(productIds, pageable);
    }
}
