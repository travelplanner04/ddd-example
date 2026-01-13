package order.framework.config;

import order.entity.model.OrderConfirmationCalculator;
import order.interfaceadapter.controller.OrderController;
import order.interfaceadapter.gateway.InMemoryOrderConfirmationGateway;
import order.interfaceadapter.gateway.InMemoryOrderGateway;
import order.interfaceadapter.gateway.ProductInfoGatewayImpl;
import order.interfaceadapter.presenter.OrderPresenter;
import order.framework.external.ExternalOrderApiGateway;
import order.framework.notification.EmailNotificationGateway;
import order.usecase.boundary.input.ConfirmOrderInputBoundary;
import order.usecase.boundary.input.GetOrderInputBoundary;
import order.usecase.boundary.input.UpdateOrderInputBoundary;
import order.usecase.boundary.output.*;
import order.usecase.dto.OrderMapper;
import order.usecase.interactor.ConfirmOrderInteractor;
import order.usecase.interactor.GetOrderInteractor;
import order.usecase.interactor.UpdateOrderInteractor;
import product.usecase.boundary.input.ReserveStockInputBoundary;
import product.usecase.boundary.output.ProductGateway;

/**
 * Configuration class for the Order module.
 * Wires together all components following Clean Architecture principles.
 */
public class OrderModuleConfiguration {

    private final OrderController orderController;
    private final OrderPresenter orderPresenter;
    private final OrderGateway orderGateway;

    public OrderModuleConfiguration(ProductGateway productGateway, ReserveStockInputBoundary reserveStockUseCase) {
        // Gateways (Output Boundaries)
        this.orderGateway = new InMemoryOrderGateway();
        OrderConfirmationGateway confirmationGateway = new InMemoryOrderConfirmationGateway();
        ExternalOrderGateway externalOrderGateway = new ExternalOrderApiGateway();
        NotificationGateway notificationGateway = new EmailNotificationGateway();
        ProductInfoGateway productInfoGateway = new ProductInfoGatewayImpl(productGateway);

        // Domain Services
        OrderConfirmationCalculator confirmationCalculator = new OrderConfirmationCalculator();

        // Mapper
        OrderMapper mapper = new OrderMapper();

        // Use Cases (Interactors)
        GetOrderInputBoundary getOrderUseCase = new GetOrderInteractor(orderGateway, productInfoGateway, mapper);
        UpdateOrderInputBoundary updateOrderUseCase = new UpdateOrderInteractor(orderGateway, mapper);
        ConfirmOrderInputBoundary confirmOrderUseCase = new ConfirmOrderInteractor(
                orderGateway,
                confirmationGateway,
                externalOrderGateway,
                notificationGateway,
                reserveStockUseCase,  // Using Product's Use Case directly (pragmatic approach)
                confirmationCalculator,
                mapper
        );

        // Controller and Presenter
        this.orderController = new OrderController(getOrderUseCase, updateOrderUseCase, confirmOrderUseCase);
        this.orderPresenter = new OrderPresenter();
    }

    public OrderController getOrderController() {
        return orderController;
    }

    public OrderPresenter getOrderPresenter() {
        return orderPresenter;
    }

    public OrderGateway getOrderGateway() {
        return orderGateway;
    }
}
