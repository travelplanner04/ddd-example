package order.domain.service;

import order.domain.model.Money;
import order.domain.model.Order;
import order.domain.model.OrderConfirmation;

import java.math.BigDecimal;

/**
 * Domain Service für Bestätigungs-Berechnung.
 *
 * Liegt im Domain Layer, da es reine Geschäftslogik ist
 * die nicht zu einem Aggregate gehört.
 */
public class OrderConfirmationCalculator {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.19");
    private static final Money BASE_SHIPPING = Money.of(5.99);
    private static final Money PER_ITEM_SHIPPING = Money.of(1.00);

    private long nextId = 1;

    /**
     * Berechnet die Bestätigungsdetails für eine Order.
     *
     * HINWEIS: Die ID-Generierung hier ist vereinfacht (In-Memory Counter).
     * In Produktion würde die ID vom Repository/IdGenerator kommen.
     */
    public OrderConfirmation calculate(Order order) {
        Money totalAmount = order.calculateTotal();
        Money taxAmount = totalAmount.multiply(TAX_RATE);
        Money shippingCost = calculateShipping(order.getItemCount());

        return OrderConfirmation.create(
            nextId++,
            order.getId(),
            totalAmount,
            taxAmount,
            shippingCost
        );
    }

    private Money calculateShipping(int itemCount) {
        return BASE_SHIPPING.add(PER_ITEM_SHIPPING.multiply(itemCount));
    }
}
