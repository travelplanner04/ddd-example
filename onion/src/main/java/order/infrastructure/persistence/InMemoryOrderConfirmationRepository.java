package order.infrastructure.persistence;

import order.application.repository.OrderConfirmationRepository;
import order.core.model.OrderConfirmation;

import java.util.HashMap;
import java.util.Map;

/**
 * Infrastructure - In-Memory Repository für OrderConfirmations.
 */
public class InMemoryOrderConfirmationRepository implements OrderConfirmationRepository {

    private final Map<Long, OrderConfirmation> database = new HashMap<>();
    private long idSequence = 1;

    @Override
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
