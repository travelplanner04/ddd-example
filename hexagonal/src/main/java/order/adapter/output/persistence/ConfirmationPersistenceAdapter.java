package order.adapter.output.persistence;

import order.application.port.output.SaveConfirmationPort;
import order.domain.model.OrderConfirmation;

import java.util.HashMap;
import java.util.Map;

/**
 * Secondary Adapter - Persistenz für Confirmations.
 *
 * Separater Adapter, da Confirmation ein eigenes Aggregate ist.
 */
public class ConfirmationPersistenceAdapter implements SaveConfirmationPort {

    private final Map<Long, OrderConfirmation> database = new HashMap<>();
    private long idSequence = 1;

    @Override
    public OrderConfirmation save(OrderConfirmation confirmation) {
        Long id = idSequence++;

        // Neue Confirmation mit generierter ID
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
