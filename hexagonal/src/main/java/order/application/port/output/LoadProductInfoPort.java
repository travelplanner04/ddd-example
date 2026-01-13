package order.application.port.output;

import order.domain.model.ProductId;

import java.util.Optional;

/**
 * Output Port - Produktinformationen laden.
 *
 * Anti-Corruption Layer: Order-Modul definiert eigenes Interface,
 * um Produktdaten zu laden, ohne direkt vom Product-Modul abzuhängen.
 */
public interface LoadProductInfoPort {

    /**
     * Lädt Produktinformationen für eine ProductId.
     * Wird bei Order-Bestätigung verwendet.
     */
    Optional<ProductInfo> loadProductInfo(ProductId productId);

    /**
     * Leichtgewichtiges DTO mit nur den benötigten Produktdaten.
     */
    record ProductInfo(
        Long productId,
        String productName,
        String manufacturer
    ) {}
}
