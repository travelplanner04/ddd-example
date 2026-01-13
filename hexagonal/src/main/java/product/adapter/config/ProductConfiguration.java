package product.adapter.config;

import product.adapter.input.rest.ProductController;
import product.adapter.output.persistence.ProductPersistenceAdapter;
import product.application.port.input.ReserveStockUseCase;
import product.application.port.output.LoadProductPort;
import product.application.port.output.SaveProductPort;
import product.application.service.ProductService;

/**
 * Konfiguration - Dependency Injection für Product-Modul.
 */
public class ProductConfiguration {

    private final ProductPersistenceAdapter productPersistenceAdapter;
    private final ProductService productService;
    private final ProductController productController;

    public ProductConfiguration() {
        // 1. Output Adapter
        this.productPersistenceAdapter = new ProductPersistenceAdapter();

        // 2. Application Service (mit beiden Ports)
        this.productService = new ProductService(
            productPersistenceAdapter,  // LoadProductPort
            productPersistenceAdapter   // SaveProductPort
        );

        // 3. Input Adapter
        this.productController = new ProductController(productService);
    }

    public ProductController productController() {
        return productController;
    }

    public ProductService productService() {
        return productService;
    }

    // Für andere Module (z.B. Order)
    public ReserveStockUseCase reserveStockUseCase() {
        return productService;
    }

    public LoadProductPort loadProductPort() {
        return productPersistenceAdapter;
    }

    public SaveProductPort saveProductPort() {
        return productPersistenceAdapter;
    }
}
