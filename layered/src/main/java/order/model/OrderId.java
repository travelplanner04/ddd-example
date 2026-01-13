package order.model;

import java.util.Objects;

/**
 * Value Object - Order ID.
 *
 * Auch in Layered Architecture sind Value Objects sinnvoll.
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
