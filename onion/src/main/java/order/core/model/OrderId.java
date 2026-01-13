package order.core.model;

import java.util.Objects;

/**
 * Value Object - Order ID.
 *
 * Im innersten Kern der Onion.
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
