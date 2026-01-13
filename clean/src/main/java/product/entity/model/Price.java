package product.entity.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing the price of a Product.
 */
public class Price {

    private final BigDecimal value;

    public Price(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Price value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Price of(double value) {
        return new Price(BigDecimal.valueOf(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + " EUR";
    }
}
