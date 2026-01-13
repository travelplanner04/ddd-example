package product.core.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object - Preis.
 */
public record Price(BigDecimal amount) {

    public static final Price ZERO = new Price(BigDecimal.ZERO);

    public Price {
        Objects.requireNonNull(amount, "Price amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Price of(BigDecimal amount) {
        return new Price(amount);
    }

    public static Price of(double amount) {
        return new Price(BigDecimal.valueOf(amount));
    }
}
