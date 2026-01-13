package order.application.dto;

import java.math.BigDecimal;

/**
 * Response DTO für OrderItem.
 */
public record OrderItemResponse(
    Long productId,
    int quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}
