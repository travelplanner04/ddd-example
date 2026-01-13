package order.usecase.boundary.output;

import order.entity.model.OrderConfirmation;

/**
 * Output Boundary (Gateway Interface) for Order Confirmation persistence.
 */
public interface OrderConfirmationGateway {

    void save(OrderConfirmation confirmation);
}
