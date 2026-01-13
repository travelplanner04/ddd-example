package order.interfaceadapter.gateway;

import order.entity.model.OrderConfirmation;
import order.entity.model.OrderId;
import order.usecase.boundary.output.OrderConfirmationGateway;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the OrderConfirmationGateway.
 */
public class InMemoryOrderConfirmationGateway implements OrderConfirmationGateway {

    private final Map<OrderId, OrderConfirmation> confirmations = new HashMap<>();

    @Override
    public void save(OrderConfirmation confirmation) {
        confirmations.put(confirmation.getOrderId(), confirmation);
        System.out.println("Saved order confirmation: " + confirmation);
    }
}
