package order.application.port.output;

import order.domain.model.Order;

/**
 * Output Port: Order in Persistenz speichern.
 */
public interface SaveOrderPort {
    Order save(Order order);
}
