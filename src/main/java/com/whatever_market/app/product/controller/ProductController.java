package com.whatever_market.app.product.controller;

import com.whatever_market.app.bible.exception.InvalidRequestException;
import com.whatever_market.app.bible.exception.NotFoundException;
import com.whatever_market.app.product.dto.ProductRequestDTO;
import com.whatever_market.app.product.dto.ProductResponseDTO;
import com.whatever_market.app.product.dto.WishProductRequestDto;
import com.whatever_market.app.product.model.Product;
import com.whatever_market.app.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        Page<ProductResponseDTO> responseDTOs = products.map(this::convertToResponseDTO);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductResponseDTO responseDTO = convertToResponseDTO(product);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product product = productService.createProduct(productRequestDTO);
        ProductResponseDTO responseDTO = convertToResponseDTO(product);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        Product product = productService.updateProduct(id, productRequestDTO);
        ProductResponseDTO responseDTO = convertToResponseDTO(product);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 찜하기
    @PostMapping("/wish")
    public ResponseEntity<Void> addWishedProduct(@RequestBody WishProductRequestDto wishProductRequestDto) {
        productService.addWishedProduct(wishProductRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 찜하기 취소
    @DeleteMapping("/wish")
    public ResponseEntity<Void> deleteWishedProduct(@RequestBody WishProductRequestDto wishProductRequestDto) {
        productService.deleteWishedProduct(wishProductRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 찜한 상품 목록 조회
    @GetMapping("/wish")
    public ResponseEntity<Page<ProductResponseDTO>> getWishedProducts(@RequestParam Long userId, Pageable pageable) {
        Page<Product> products = productService.getWishedProducts(userId, pageable);
        Page<ProductResponseDTO> responseDTOs = products.map(this::convertToResponseDTO);
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO responseDTO = new ProductResponseDTO();

        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setImageURL(product.getImageURL());
        responseDTO.setPrice(product.getPrice());
        responseDTO.setDescription(product.getDescription());

        responseDTO.setCreatedAt(product.getCreatedAt());
        responseDTO.setModifiedAt(product.getModifiedAt());

        return responseDTO;
    }
}
