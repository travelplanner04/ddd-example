package order.entity.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain Service for calculating order confirmation details.
 * This is part of the Entity layer as it contains enterprise business rules.
 */
public class OrderConfirmationCalculator {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.19");
    private static final Money FREE_SHIPPING_THRESHOLD = Money.of(100.00);
    private static final Money BASE_SHIPPING_COST = Money.of(5.99);
    private static final Money PER_ITEM_SHIPPING_COST = Money.of(1.00);

    public OrderConfirmation calculate(Order order) {
        Money subtotal = order.calculateTotal();
        Money tax = calculateTax(subtotal);
        Money shipping = calculateShipping(order, subtotal);
        Money total = subtotal.add(tax).add(shipping);

        return new OrderConfirmation(
                order.getId(),
                LocalDateTime.now(),
                subtotal,
                tax,
                shipping,
                total
        );
    }

    private Money calculateTax(Money subtotal) {
        return subtotal.multiply(TAX_RATE);
    }

    private Money calculateShipping(Order order, Money subtotal) {
        if (subtotal.isGreaterThanOrEqual(FREE_SHIPPING_THRESHOLD)) {
            return Money.zero();
        }
        return BASE_SHIPPING_COST.add(PER_ITEM_SHIPPING_COST.multiply(order.getTotalItemCount()));
    }
}
