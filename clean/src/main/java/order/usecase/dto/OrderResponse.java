package order.usecase.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for an order.
 */
public record OrderResponse(
        Long orderId,
        String customerId,
        String status,
        List<OrderItemResponse> items,
        BigDecimal total
) {}
