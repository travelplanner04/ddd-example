package order.application.port.output;

import order.domain.model.OrderConfirmation;

/**
 * Output Port: Confirmation in Persistenz speichern.
 */
public interface SaveConfirmationPort {
    OrderConfirmation save(OrderConfirmation confirmation);
}
