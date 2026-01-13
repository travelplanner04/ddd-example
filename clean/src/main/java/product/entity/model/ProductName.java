package product.entity.model;

import java.util.Objects;

/**
 * Value Object representing the name of a Product.
 */
public class ProductName {

    private final String value;

    public ProductName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductName value cannot be null or blank");
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
        ProductName that = (ProductName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
