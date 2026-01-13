package product.application.service;

import product.application.dto.ProductResponse;
import product.application.mapper.ProductMapper;
import product.application.port.input.GetProductUseCase;
import product.application.port.input.ReserveStockUseCase;
import product.application.port.output.LoadProductPort;
import product.application.port.output.SaveProductPort;
import product.domain.exception.ProductNotFoundException;
import product.domain.model.Product;
import product.domain.model.ProductId;

import java.util.List;

/**
 * Application Service - orchestriert Product Use Cases.
 *
 * Exponiert Stock-Operationen für andere Module (z.B. Order).
 */
public class ProductService implements GetProductUseCase, ReserveStockUseCase {

    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;
    private final ProductMapper mapper;

    public ProductService(LoadProductPort loadProductPort, SaveProductPort saveProductPort) {
        this.loadProductPort = loadProductPort;
        this.saveProductPort = saveProductPort;
        this.mapper = new ProductMapper();
    }

    @Override
    public ProductResponse getProduct(ProductId productId) {
        Product product = loadProductPort.loadById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
        return mapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = loadProductPort.loadAll();
        return mapper.toResponseList(products);
    }

    // ==================== STOCK USE CASE ====================

    @Override
    public void reserveStock(ProductId productId, int quantity) {
        Product product = loadProductPort.loadById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        // Domain-Logik aufrufen (wirft InsufficientStockException wenn nötig)
        product.reserveStock(quantity);

        saveProductPort.save(product);
    }

    @Override
    public void releaseStock(ProductId productId, int quantity) {
        Product product = loadProductPort.loadById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        product.releaseStock(quantity);

        saveProductPort.save(product);
    }
}
