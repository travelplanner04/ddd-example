package product.domain.model;

import java.util.Objects;

/**
 * Value Object - Product ID.
 *
 * Eigenes ProductId im Product-Modul (NICHT geteilt mit Order!).
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
