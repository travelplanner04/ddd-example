package order.usecase.interactor;

import order.entity.exception.OrderAlreadyExistsException;
import order.entity.exception.OrderNotFoundException;
import order.entity.model.*;
import order.usecase.boundary.input.ConfirmOrderInputBoundary;
import order.usecase.boundary.output.*;
import order.usecase.dto.OrderConfirmationResponse;
import order.usecase.dto.OrderMapper;
import product.usecase.boundary.input.ReserveStockInputBoundary;

/**
 * Interactor (Use Case Implementation) for confirming an Order.
 * This interactor orchestrates the complete order confirmation workflow.
 */
public class ConfirmOrderInteractor implements ConfirmOrderInputBoundary {

    private final OrderGateway orderGateway;
    private final OrderConfirmationGateway confirmationGateway;
    private final ExternalOrderGateway externalOrderGateway;
    private final NotificationGateway notificationGateway;
    private final ReserveStockInputBoundary reserveStockUseCase;
    private final OrderConfirmationCalculator confirmationCalculator;
    private final OrderMapper mapper;

    public ConfirmOrderInteractor(
            OrderGateway orderGateway,
            OrderConfirmationGateway confirmationGateway,
            ExternalOrderGateway externalOrderGateway,
            NotificationGateway notificationGateway,
            ReserveStockInputBoundary reserveStockUseCase,
            OrderConfirmationCalculator confirmationCalculator,
            OrderMapper mapper) {
        this.orderGateway = orderGateway;
        this.confirmationGateway = confirmationGateway;
        this.externalOrderGateway = externalOrderGateway;
        this.notificationGateway = notificationGateway;
        this.reserveStockUseCase = reserveStockUseCase;
        this.confirmationCalculator = confirmationCalculator;
        this.mapper = mapper;
    }

    @Override
    public OrderConfirmationResponse execute(Long orderId) {
        OrderId id = new OrderId(orderId);

        // 1. Load the order
        Order order = orderGateway.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // 2. Check if order already exists in external system
        if (externalOrderGateway.existsInExternalSystem(id)) {
            throw new OrderAlreadyExistsException(id);
        }

        // 3. Reserve stock for each item (using Product's Use Case directly - pragmatic approach)
        order.getItems().forEach(item -> {
            reserveStockUseCase.execute(
                    item.getProductId().getValue(),
                    item.getQuantity().getValue()
            );
        });

        // 4. Confirm the order (domain logic)
        order.confirm();

        // 5. Calculate confirmation details
        OrderConfirmation confirmation = confirmationCalculator.calculate(order);

        // 6. Save the order and confirmation
        orderGateway.save(order);
        confirmationGateway.save(confirmation);

        // 7. Send notification
        notificationGateway.sendOrderConfirmation(confirmation, order.getCustomerId().getValue() + "@example.com");

        return mapper.toConfirmationResponse(confirmation);
    }
}
