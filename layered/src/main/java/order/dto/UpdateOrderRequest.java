package order.dto;

import java.util.List;

/**
 * Request DTO für Order-Aktualisierung.
 */
public record UpdateOrderRequest(
    List<OrderItemRequest> items
) {}
