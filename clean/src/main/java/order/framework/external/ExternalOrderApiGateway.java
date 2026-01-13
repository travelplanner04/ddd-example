package order.framework.external;

import order.entity.model.OrderId;
import order.usecase.boundary.output.ExternalOrderGateway;

/**
 * Framework layer implementation for external order API integration.
 * In Clean Architecture, Framework & Drivers is the outermost layer
 * containing all external dependencies (web frameworks, databases, etc.).
 */
public class ExternalOrderApiGateway implements ExternalOrderGateway {

    @Override
    public boolean existsInExternalSystem(OrderId orderId) {
        // Simulate external API call
        System.out.println("Checking if order " + orderId + " exists in external system...");

        // For demo purposes, order IDs > 1000 are considered to exist in external system
        return orderId.getValue() > 1000;
    }
}
