package product.application.service;

import product.application.repository.ProductRepository;
import product.core.exception.ProductNotFoundException;
import product.core.model.Product;
import product.core.model.ProductId;

/**
 * Application Service - Stock-Operationen.
 *
 * Product exponiert diesen Service für andere Module (z.B. Order).
 * Pragmatisch: Keine ACL nötig - andere Module nutzen dies direkt.
 */
public class ProductStockService {

    private final ProductRepository productRepository;

    public ProductStockService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Reserviert Lagerbestand.
     *
     * @throws product.core.exception.InsufficientStockException wenn nicht genug Bestand
     */
    public void reserveStock(ProductId productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        // Domain-Logik aufrufen
        product.reserveStock(quantity);

        productRepository.save(product);
    }

    /**
     * Gibt reservierten Bestand zurück.
     */
    public void releaseStock(ProductId productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        product.releaseStock(quantity);

        productRepository.save(product);
    }
}
