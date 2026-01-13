package product.infrastructure.config;

import product.application.ProductApplicationService;
import product.application.repository.ProductRepository;
import product.application.service.ProductStockService;
import product.infrastructure.persistence.InMemoryProductRepository;
import product.infrastructure.web.ProductController;

/**
 * Konfiguration - Dependency Injection für Product-Modul.
 */
public class ProductModuleConfiguration {

    private final ProductRepository productRepository;
    private final ProductApplicationService productService;
    private final ProductStockService productStockService;
    private final ProductController productController;

    public ProductModuleConfiguration() {
        this.productRepository = new InMemoryProductRepository();
        this.productService = new ProductApplicationService(productRepository);
        this.productStockService = new ProductStockService(productRepository);
        this.productController = new ProductController(productService);
    }

    public ProductController productController() {
        return productController;
    }

    public ProductApplicationService productService() {
        return productService;
    }

    // Für andere Module (z.B. Order)
    public ProductStockService productStockService() {
        return productStockService;
    }

    public ProductRepository productRepository() {
        return productRepository;
    }
}
