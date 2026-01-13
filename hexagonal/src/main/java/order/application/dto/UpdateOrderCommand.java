package order.application.dto;

import java.util.List;

/**
 * Command DTO für Order Update.
 */
public record UpdateOrderCommand(
    List<OrderItemRequest> items
) {
    public UpdateOrderCommand {
        if (items == null) {
            throw new IllegalArgumentException("items cannot be null");
        }
    }
}
