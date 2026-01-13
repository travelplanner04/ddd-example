package order.application.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO für Order.
 */
public record OrderResponse(
    Long id,
    String customerId,
    String status,
    List<OrderItemResponse> items,
    BigDecimal totalAmount
) {}
