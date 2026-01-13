package product.application.port.input;

import product.domain.model.ProductId;

/**
 * Input Port - Stock-Reservierung.
 *
 * Product exponiert diesen Use Case für andere Module.
 * Pragmatisch: Keine ACL nötig - andere Module nutzen dies direkt.
 */
public interface ReserveStockUseCase {

    /**
     * Reserviert Lagerbestand.
     *
     * @throws product.domain.exception.InsufficientStockException wenn nicht genug Bestand
     */
    void reserveStock(ProductId productId, int quantity);

    /**
     * Gibt reservierten Bestand zurück.
     */
    void releaseStock(ProductId productId, int quantity);
}
