package order.application.service;

import order.core.model.ProductId;

import java.util.Optional;

/**
 * Application Service Interface - Produktinformationen.
 *
 * In Onion: Interface ist in Application Layer definiert,
 * Implementierung in Infrastructure.
 */
public interface ProductInfoService {

    /**
     * Lädt Produktinformationen für eine ProductId.
     */
    Optional<ProductInfo> getProductInfo(ProductId productId);

    /**
     * DTO mit den benötigten Produktdaten.
     */
    record ProductInfo(
        Long productId,
        String productName,
        String manufacturer
    ) {}
}
