package order.application.port.output;

import order.domain.model.OrderId;

/**
 * Output Port: Prüfen ob Order in externem System existiert.
 */
public interface CheckOrderExistsPort {
    boolean existsInExternalSystem(OrderId orderId);
}
