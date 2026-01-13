package order.repository;

import order.model.OrderConfirmation;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Layer - OrderConfirmation Repository.
 */
public class OrderConfirmationRepository {

    private final Map<Long, OrderConfirmation> database = new HashMap<>();
    private long idSequence = 1;

    public OrderConfirmation save(OrderConfirmation confirmation) {
        Long id = idSequence++;

        OrderConfirmation persisted = OrderConfirmation.create(
            id,
            confirmation.getOrderId(),
            confirmation.getTotalAmount(),
            confirmation.getTaxAmount(),
            confirmation.getShippingCost(),
            confirmation.getConfirmedAt()
        );

        database.put(id, persisted);
        return persisted;
    }
}
