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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


    // API : 상품 상세 페이지 조회
    // 쿠키를 기반으로 하루에 한번 조회수 증가
    @Override
    public Product getProductById(Long id, HttpServletRequest request, HttpServletResponse response) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found")
        );
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;

        for (int i = 0; cookies != null && i < cookies.length; i++) {
            if (cookies[i].getName().equals("productView")) {
                cookie = cookies[i];
                if (!cookie.getValue().contains("[" + product.getId() + "]")) {
                    product.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + product.getId() + "]");
                }
                isCookie = true;
                break;
            }
        }
        if (!isCookie) {
            product.addViewCount();
            cookie = new Cookie("productView", "[" + product.getId() + "]"); // oldCookie에 새 쿠키 생성
        }

        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/");
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
        return product;
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
    @Transactional(readOnly = true)
    public Page<Product> getWishedProducts(Long userId, Pageable pageable) {
        List<WishList> wishListLists = wishListRepository.findAllByUserId(userId);
        List<Long> productIds = wishListLists.stream()
                .map(wishList -> wishList.getProduct().getId())
                .collect(Collectors.toList());
        return productRepository.findAllByIdIn(productIds, pageable);
    }
}
