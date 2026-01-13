package order.application.port.input;

import order.application.dto.OrderConfirmationResponse;
import order.domain.model.OrderId;

/**
 * Input Port: Use Case für Order bestätigen.
 */
public interface ConfirmOrderUseCase {
    OrderConfirmationResponse confirmOrder(OrderId orderId);
}
