package product.entity.model;

import java.util.Objects;

/**
 * Value Object representing the unique identifier of a Product.
 */
public class ProductId {

    private final Long value;

    public ProductId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ProductId value cannot be null");
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
        ProductId productId = (ProductId) o;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ProductId{" + value + "}";
    }
}
