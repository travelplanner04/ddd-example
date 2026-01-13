package order.application.service;

import order.core.model.OrderId;

/**
 * Application Service Interface - Externe API.
 *
 * Für Prüfung gegen externes System.
 */
public interface ExternalOrderService {

    boolean existsInExternalSystem(OrderId orderId);
}
