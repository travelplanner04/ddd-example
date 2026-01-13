package order.entity.model;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of an Order.
 */
public class OrderId {

    private final Long value;

    public OrderId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("OrderId value cannot be null");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return Objects.equals(value, orderId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "OrderId{" + value + "}";
    }
}
