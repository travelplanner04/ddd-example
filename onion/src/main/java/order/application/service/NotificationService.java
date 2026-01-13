package order.application.service;

import order.core.model.CustomerId;
import order.core.model.OrderConfirmation;

/**
 * Application Service Interface - Benachrichtigungen.
 *
 * Interface in Application, Implementierung in Infrastructure.
 */
public interface NotificationService {

    void sendOrderConfirmation(CustomerId customerId, OrderConfirmation confirmation);
}
