package order.domain.model;

import java.util.Objects;

/**
 * Value Object für Order ID.
 * Typsicherheit statt primitive Long.
 */
public record OrderId(Long value) {

    public OrderId {
        Objects.requireNonNull(value, "OrderId cannot be null");
        if (value <= 0) {
            throw new IllegalArgumentException("OrderId must be positive");
        }
    }

    public static OrderId of(Long value) {
        return new OrderId(value);
    }
}
