package order.usecase.interactor;

import order.entity.exception.OrderNotFoundException;
import order.entity.model.*;
import order.usecase.boundary.input.UpdateOrderInputBoundary;
import order.usecase.boundary.output.OrderGateway;
import order.usecase.dto.OrderMapper;
import order.usecase.dto.OrderResponse;
import order.usecase.dto.UpdateOrderRequest;

/**
 * Interactor (Use Case Implementation) for updating an Order.
 */
public class UpdateOrderInteractor implements UpdateOrderInputBoundary {

    private final OrderGateway orderGateway;
    private final OrderMapper mapper;

    public UpdateOrderInteractor(OrderGateway orderGateway, OrderMapper mapper) {
        this.orderGateway = orderGateway;
        this.mapper = mapper;
    }

    @Override
    public OrderResponse execute(Long orderId, UpdateOrderRequest request) {
        OrderId id = new OrderId(orderId);

        Order order = orderGateway.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Clear existing items and add new ones
        order.getItems().forEach(item -> order.removeItem(item.getProductId()));

        request.items().forEach(itemRequest -> {
            order.addItem(
                    new ProductId(itemRequest.productId()),
                    new Quantity(itemRequest.quantity()),
                    new Money(itemRequest.price())
            );
        });

        Order savedOrder = orderGateway.save(order);
        return mapper.toResponse(savedOrder);
    }
}
