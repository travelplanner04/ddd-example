package order.core.model;

import java.util.Objects;

/**
 * Value Object - Customer ID.
 */
public record CustomerId(String value) {

    public CustomerId {
        Objects.requireNonNull(value, "CustomerId cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("CustomerId cannot be blank");
        }
    }

    public static CustomerId of(String value) {
        return new CustomerId(value);
    }
}
