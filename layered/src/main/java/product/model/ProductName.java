package product.model;

import java.util.Objects;

/**
 * Value Object - Produktname.
 */
public record ProductName(String value) {

    public ProductName {
        Objects.requireNonNull(value, "ProductName cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("ProductName cannot be empty");
        }
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
