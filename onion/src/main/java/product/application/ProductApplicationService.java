package product.application;

import product.application.dto.ProductResponse;
import product.application.mapper.ProductMapper;
import product.application.repository.ProductRepository;
import product.core.exception.ProductNotFoundException;
import product.core.model.Product;
import product.core.model.ProductId;

import java.util.List;

/**
 * Application Service - Product Use Cases.
 */
public class ProductApplicationService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    public ProductApplicationService(ProductRepository productRepository) {
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
