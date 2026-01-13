package order.application.mapper;

import order.application.dto.*;
import order.domain.model.*;

import java.util.List;

/**
 * Mapper zwischen Domain und DTOs.
 * Kapselt die Transformation.
 */
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
            order.getId().value(),
            order.getCustomerId().value(),
            order.getStatus().name(),
            toItemResponses(order.getItems()),
            order.calculateTotal().amount()
        );
    }

    public OrderConfirmationResponse toResponse(OrderConfirmation confirmation) {
        return new OrderConfirmationResponse(
            confirmation.getId(),
            confirmation.getOrderId().value(),
            confirmation.getTotalAmount().amount(),
            confirmation.getTaxAmount().amount(),
            confirmation.getShippingCost().amount(),
            confirmation.getGrandTotal().amount(),
            confirmation.getConfirmedAt()
        );
    }

    public List<OrderItem> toDomainItems(List<OrderItemRequest> requests) {
        return requests.stream()
            .map(this::toDomainItem)
            .toList();
    }

    private OrderItem toDomainItem(OrderItemRequest request) {
        return OrderItem.create(
            ProductId.of(request.productId()),
            Quantity.of(request.quantity()),
            Money.of(request.unitPrice())
        );
    }

    private List<OrderItemResponse> toItemResponses(List<OrderItem> items) {
        return items.stream()
            .map(this::toItemResponse)
            .toList();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
            item.getProductId().value(),
            item.getQuantity().value(),
            item.getUnitPrice().amount(),
            item.calculateSubtotal().amount()
        );
    }
}
