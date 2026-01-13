package order.application.dto;

import java.util.List;

/**
 * Command DTO für Order-Aktualisierung.
 */
public record UpdateOrderCommand(
    List<OrderItemRequest> items
) {}
