package order.application.repository;

import order.core.model.OrderConfirmation;

/**
 * Repository Interface - OrderConfirmation.
 */
public interface OrderConfirmationRepository {

    OrderConfirmation save(OrderConfirmation confirmation);
}
