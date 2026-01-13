package order.domain.model;

import java.util.Objects;

/**
 * Value Object für Product ID.
 * WICHTIG: Liegt im Order-Modul, NICHT importiert aus Product-Modul!
 * Das ist der Anti-Corruption Layer - Order kennt nur die ID, nicht das Product.
 */
public record ProductId(Long value) {

    public ProductId {
        Objects.requireNonNull(value, "ProductId cannot be null");
        if (value <= 0) {
            throw new IllegalArgumentException("ProductId must be positive");
        }
    }

    public static ProductId of(Long value) {
        return new ProductId(value);
    }
}
