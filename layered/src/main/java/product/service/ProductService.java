package product.service;

import product.dto.ProductResponse;
import product.exception.ProductNotFoundException;
import product.model.Product;
import product.model.ProductId;
import product.repository.ProductRepository;

import java.util.List;

/**
 * Service Layer - Product Geschäftslogik.
 */
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.mapper = new ProductMapper();
    }

    public ProductResponse getProduct(ProductId productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
        return mapper.toResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return mapper.toResponseList(products);
    }
}
