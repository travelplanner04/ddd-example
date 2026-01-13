package order.service;

import order.model.Money;
import order.model.Order;
import order.model.OrderConfirmation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service Layer - Berechnung der Bestellbestätigung.
 */
public class OrderConfirmationCalculator {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.19");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("100.00");
    private static final Money STANDARD_SHIPPING = Money.of(new BigDecimal("5.99"));

    public OrderConfirmation calculate(Order order) {
        Money totalAmount = order.calculateTotal();
        Money taxAmount = calculateTax(totalAmount);
        Money shippingCost = calculateShipping(totalAmount);

        return OrderConfirmation.create(
            null,
            order.getId(),
            totalAmount,
            taxAmount,
            shippingCost,
            LocalDateTime.now()
        );
    }

    private Money calculateTax(Money amount) {
        return amount.multiply(TAX_RATE);
    }

    private Money calculateShipping(Money orderTotal) {
        if (orderTotal.amount().compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return Money.ZERO;
        }
        return STANDARD_SHIPPING;
    }
}
