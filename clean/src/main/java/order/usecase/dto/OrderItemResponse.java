package order.usecase.dto;

import java.math.BigDecimal;

/**
 * Response DTO for an order item.
 */
public record OrderItemResponse(
        Long productId,
        int quantity,
        BigDecimal price,
        BigDecimal total
) {}
