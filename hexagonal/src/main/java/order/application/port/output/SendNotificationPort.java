package order.application.port.output;

import order.domain.model.CustomerId;
import order.domain.model.OrderConfirmation;

/**
 * Output Port: Benachrichtigung senden.
 */
public interface SendNotificationPort {
    void sendOrderConfirmation(CustomerId customerId, OrderConfirmation confirmation);
}
