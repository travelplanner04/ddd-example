package order.infrastructure.config;

import order.application.OrderApplicationService;
import order.application.repository.OrderConfirmationRepository;
import order.application.repository.OrderRepository;
import order.application.service.ExternalOrderService;
import order.application.service.NotificationService;
import order.application.service.ProductInfoService;
import order.infrastructure.external.ExternalOrderApiService;
import order.infrastructure.notification.EmailNotificationService;
import order.infrastructure.persistence.InMemoryOrderConfirmationRepository;
import order.infrastructure.persistence.InMemoryOrderRepository;
import order.infrastructure.product.ProductInfoServiceImpl;
import order.infrastructure.web.OrderController;
import product.application.service.ProductStockService;  // Product's Service direkt!

/**
 * Konfiguration - Dependency Injection.
 *
 * PRAGMATISCH: Nimmt Product's Service direkt entgegen.
 * Kein eigener Adapter/Impl für Stock.
 */
public class OrderModuleConfiguration {

    private final OrderRepository orderRepository;
    private final OrderConfirmationRepository confirmationRepository;
    private final NotificationService notificationService;
    private final ExternalOrderService externalOrderService;
    private final ProductInfoService productInfoService;
    private final OrderApplicationService orderService;
    private final OrderController orderController;

    /**
     * Konstruktor mit Abhängigkeiten zum Product-Modul.
     *
     * PRAGMATISCH: Nimmt Product's ProductStockService direkt!
     */
    public OrderModuleConfiguration(
            product.application.repository.ProductRepository productRepository,
            ProductStockService productStockService) {

        // Infrastructure Layer
        this.orderRepository = new InMemoryOrderRepository();
        this.confirmationRepository = new InMemoryOrderConfirmationRepository();
        this.notificationService = new EmailNotificationService();
        this.externalOrderService = new ExternalOrderApiService();
        this.productInfoService = new ProductInfoServiceImpl(productRepository);

        // Application Layer - nutzt Product's Service direkt!
        this.orderService = new OrderApplicationService(
            orderRepository,
            confirmationRepository,
            notificationService,
            externalOrderService,
            productInfoService,
            productStockService  // Product's Service direkt!
        );

        // Web Layer
        this.orderController = new OrderController(orderService);
    }

    public OrderController orderController() {
        return orderController;
    }

    public OrderApplicationService orderService() {
        return orderService;
    }

    public OrderRepository orderRepository() {
        return orderRepository;
    }
}
