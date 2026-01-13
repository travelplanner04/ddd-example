package order.usecase.dto;

import java.util.List;

/**
 * Request DTO for updating an order.
 */
public record UpdateOrderRequest(
        List<OrderItemRequest> items
) {}
