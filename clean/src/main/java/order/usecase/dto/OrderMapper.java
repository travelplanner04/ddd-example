package order.usecase.dto;

import order.entity.model.Order;
import order.entity.model.OrderConfirmation;
import order.entity.model.OrderItem;

import java.util.List;

/**
 * Mapper for converting between domain entities and DTOs.
 */
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return new OrderResponse(
                order.getId().getValue(),
                order.getCustomerId().getValue(),
                order.getStatus().name(),
                itemResponses,
                order.calculateTotal().getAmount()
        );
    }

    public OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId().getValue(),
                item.getQuantity().getValue(),
                item.getPrice().getAmount(),
                item.calculateTotal().getAmount()
        );
    }

    public OrderConfirmationResponse toConfirmationResponse(OrderConfirmation confirmation) {
        return new OrderConfirmationResponse(
                confirmation.getOrderId().getValue(),
                confirmation.getConfirmedAt(),
                confirmation.getSubtotal().getAmount(),
                confirmation.getTax().getAmount(),
                confirmation.getShipping().getAmount(),
                confirmation.getTotal().getAmount()
        );
    }
}
