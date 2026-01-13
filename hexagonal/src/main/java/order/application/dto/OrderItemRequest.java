package order.application.dto;

import java.math.BigDecimal;

/**
 * DTO für OrderItem in Requests.
 * Immutable Record.
 */
public record OrderItemRequest(
    Long productId,
    int quantity,
    BigDecimal unitPrice
) {
    public OrderItemRequest {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("unitPrice must be positive");
        }
    }
}
