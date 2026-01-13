package order.adapter.config;

import order.adapter.input.rest.OrderController;
import order.adapter.output.external.ExternalOrderApiAdapter;
import order.adapter.output.notification.EmailNotificationAdapter;
import order.adapter.output.persistence.ConfirmationPersistenceAdapter;
import order.adapter.output.persistence.OrderPersistenceAdapter;
import order.adapter.output.product.ProductInfoAdapter;
import order.application.port.output.*;
import order.application.service.OrderService;
import product.application.port.input.ReserveStockUseCase;
import product.application.port.output.LoadProductPort;

/**
 * Konfiguration - Dependency Injection.
 *
 * PRAGMATISCH: Nimmt Product's Use Case direkt entgegen.
 * Kein eigener Adapter für Stock - Product verwaltet das selbst.
 */
public class OrderConfiguration {

    private final OrderPersistenceAdapter orderPersistenceAdapter;
    private final ConfirmationPersistenceAdapter confirmationPersistenceAdapter;
    private final EmailNotificationAdapter emailNotificationAdapter;
    private final ExternalOrderApiAdapter externalOrderApiAdapter;
    private final ProductInfoAdapter productInfoAdapter;
    private final OrderService orderService;
    private final OrderController orderController;

    /**
     * Konstruktor mit Abhängigkeiten zum Product-Modul.
     *
     * PRAGMATISCH: Nimmt Product's ReserveStockUseCase direkt!
     */
    public OrderConfiguration(LoadProductPort loadProductPort, ReserveStockUseCase reserveStockUseCase) {
        // 1. Output Adapters
        this.orderPersistenceAdapter = new OrderPersistenceAdapter();
        this.confirmationPersistenceAdapter = new ConfirmationPersistenceAdapter();
        this.emailNotificationAdapter = new EmailNotificationAdapter();
        this.externalOrderApiAdapter = new ExternalOrderApiAdapter();
        this.productInfoAdapter = new ProductInfoAdapter(loadProductPort);

        // 2. Application Service - nutzt Product's Use Case direkt!
        this.orderService = new OrderService(
            orderPersistenceAdapter,
            orderPersistenceAdapter,
            confirmationPersistenceAdapter,
            externalOrderApiAdapter,
            emailNotificationAdapter,
            productInfoAdapter,
            reserveStockUseCase  // Product's Use Case direkt!
        );

        // 3. Input Adapter
        this.orderController = new OrderController(
            orderService,
            orderService,
            orderService
        );
    }

    public OrderController orderController() {
        return orderController;
    }

    public OrderService orderService() {
        return orderService;
    }

    public LoadOrderPort loadOrderPort() {
        return orderPersistenceAdapter;
    }

    public SaveOrderPort saveOrderPort() {
        return orderPersistenceAdapter;
    }

    public SendNotificationPort sendNotificationPort() {
        return emailNotificationAdapter;
    }

    public CheckOrderExistsPort checkOrderExistsPort() {
        return externalOrderApiAdapter;
    }
}
