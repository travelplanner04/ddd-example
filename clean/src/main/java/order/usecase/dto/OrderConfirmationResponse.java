package order.usecase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for an order confirmation.
 */
public record OrderConfirmationResponse(
        Long orderId,
        LocalDateTime confirmedAt,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal shipping,
        BigDecimal total
) {}
