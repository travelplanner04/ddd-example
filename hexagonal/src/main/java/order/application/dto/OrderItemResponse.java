package order.application.dto;

import java.math.BigDecimal;

/**
 * DTO für OrderItem in Responses.
 */
public record OrderItemResponse(
    Long productId,
    int quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}
