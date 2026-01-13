package product.presentation;

import product.dto.ProductResponse;
import product.model.ProductId;
import product.service.ProductService;

import java.util.List;

/**
 * Presentation Layer - REST Controller für Products.
 */
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
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
