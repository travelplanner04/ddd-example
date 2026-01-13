package product.infrastructure.web;

import product.application.ProductApplicationService;
import product.application.dto.ProductResponse;
import product.core.model.ProductId;

import java.util.List;

/**
 * Infrastructure - REST Controller für Products.
 */
public class ProductController {

    private final ProductApplicationService productService;

    public ProductController(ProductApplicationService productService) {
        this.productService = productService;
    }

    // GET /products/{id}
    public ProductResponse getProduct(Long id) {
        return productService.getProduct(ProductId.of(id));
    }

    // GET /products
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
}
