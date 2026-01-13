package order.config;

import order.presentation.OrderController;
import order.repository.OrderConfirmationRepository;
import order.repository.OrderRepository;
import order.service.EmailService;
import order.service.ExternalOrderApiService;
import order.service.OrderService;

/**
 * Konfiguration - Dependency Injection.
 *
 * Verdrahtet alle Schichten.
 */
public class OrderConfiguration {

    private final OrderRepository orderRepository;
    private final OrderConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    private final ExternalOrderApiService externalApiService;
    private final OrderService orderService;
    private final OrderController orderController;

    /**
     * Konstruktor mit Abhängigkeit zum Product-Modul.
     * In Layered: Direkte Abhängigkeit zu konkreter Klasse.
     */
    public OrderConfiguration(product.repository.ProductRepository productRepository) {
        // Data Access Layer
        this.orderRepository = new OrderRepository();
        this.confirmationRepository = new OrderConfirmationRepository();

        // Service Layer (externe Dienste)
        this.emailService = new EmailService();
        this.externalApiService = new ExternalOrderApiService();

        // Service Layer (Hauptservice)
        this.orderService = new OrderService(
            orderRepository,
            confirmationRepository,
            emailService,
            externalApiService,
            productRepository
        );

        // Presentation Layer
        this.orderController = new OrderController(orderService);
    }

    public OrderController orderController() {
        return orderController;
    }

    public OrderService orderService() {
        return orderService;
    }

    public OrderRepository orderRepository() {
        return orderRepository;
    }
}
