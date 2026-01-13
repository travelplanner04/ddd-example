package product.config;

import product.presentation.ProductController;
import product.repository.ProductRepository;
import product.service.ProductService;

/**
 * Konfiguration - Dependency Injection für Product-Modul.
 */
public class ProductConfiguration {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ProductController productController;

    public ProductConfiguration() {
        this.productRepository = new ProductRepository();
        this.productService = new ProductService(productRepository);
        this.productController = new ProductController(productService);
    }

    public ProductController productController() {
        return productController;
    }

    public ProductService productService() {
        return productService;
    }

    public ProductRepository productRepository() {
        return productRepository;
    }
}
