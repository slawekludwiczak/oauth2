package com.ludigi.oauth2_resource_server.product;

import com.ludigi.oauth2_resource_server.product.dto.NewProductDto;
import com.ludigi.oauth2_resource_server.product.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getPrice()))
                .toList();
    }

    public ProductDto create(NewProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        Product savedProduct = productRepository.save(product);
        return new ProductDto(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
    }
}
