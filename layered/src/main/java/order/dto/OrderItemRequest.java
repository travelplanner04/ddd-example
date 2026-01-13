package order.dto;

import java.math.BigDecimal;

/**
 * Request DTO für OrderItem.
 */
public record OrderItemRequest(
    Long productId,
    int quantity,
    BigDecimal unitPrice
) {}
