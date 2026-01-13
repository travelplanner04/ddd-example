package order.entity.model;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a Customer.
 */
public class CustomerId {

    private final String value;

    public CustomerId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CustomerId value cannot be null or blank");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "CustomerId{" + value + "}";
    }
}
