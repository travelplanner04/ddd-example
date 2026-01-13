package order.domain.model;

/**
 * Value Object für Mengen.
 */
public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
