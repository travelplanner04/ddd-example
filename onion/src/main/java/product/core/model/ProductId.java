package product.core.model;

import java.util.Objects;

/**
 * Value Object - Product ID.
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
