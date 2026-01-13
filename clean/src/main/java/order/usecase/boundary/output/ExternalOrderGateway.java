package order.usecase.boundary.output;

import order.entity.model.OrderId;

/**
 * Output Boundary (Gateway Interface) for external Order system integration.
 */
public interface ExternalOrderGateway {

    boolean existsInExternalSystem(OrderId orderId);
}
