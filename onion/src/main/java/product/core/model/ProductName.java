package product.core.model;

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
        if (trimmed.length() > 200) {
            throw new IllegalArgumentException("ProductName cannot exceed 200 characters");
        }
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
