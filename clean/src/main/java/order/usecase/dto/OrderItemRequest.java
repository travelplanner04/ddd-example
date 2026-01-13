package order.usecase.dto;

import java.math.BigDecimal;

/**
 * Request DTO for an order item.
 */
public record OrderItemRequest(
        Long productId,
        int quantity,
        BigDecimal price
) {}
