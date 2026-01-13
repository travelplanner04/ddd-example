package order.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO für Order Confirmation.
 */
public record OrderConfirmationResponse(
    Long confirmationId,
    Long orderId,
    BigDecimal totalAmount,
    BigDecimal taxAmount,
    BigDecimal shippingCost,
    BigDecimal grandTotal,
    LocalDateTime confirmedAt
) {}
