package order.application;

import order.application.dto.*;
import order.application.mapper.OrderMapper;
import order.application.repository.OrderConfirmationRepository;
import order.application.repository.OrderRepository;
import order.application.service.ExternalOrderService;
import order.application.service.NotificationService;
import order.application.service.ProductInfoService;
import order.core.exception.OrderAlreadyExistsException;
import order.core.exception.OrderNotFoundException;
import order.core.model.*;
import order.domainservices.OrderConfirmationCalculator;
import product.application.service.ProductStockService;  // Product's Service direkt!

import java.util.List;

/**
 * Application Service - Orchestriert Use Cases.
 *
 * PRAGMATISCH: Nutzt Product's ProductStockService direkt!
 * Kein eigenes Interface für Cross-Module-Kommunikation.
 */
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final OrderConfirmationRepository confirmationRepository;
    private final NotificationService notificationService;
    private final ExternalOrderService externalOrderService;
    private final ProductInfoService productInfoService;
    private final ProductStockService productStockService;  // Product's Service!
    private final OrderConfirmationCalculator calculator;
    private final OrderMapper mapper;

    public OrderApplicationService(
            OrderRepository orderRepository,
            OrderConfirmationRepository confirmationRepository,
            NotificationService notificationService,
            ExternalOrderService externalOrderService,
            ProductInfoService productInfoService,
            ProductStockService productStockService) {
        this.orderRepository = orderRepository;
        this.confirmationRepository = confirmationRepository;
        this.notificationService = notificationService;
        this.externalOrderService = externalOrderService;
        this.productInfoService = productInfoService;
        this.productStockService = productStockService;
        this.calculator = new OrderConfirmationCalculator();
        this.mapper = new OrderMapper();
    }

    public OrderResponse getOrder(OrderId orderId) {
        Order order = findOrder(orderId);
        return mapper.toResponse(order);
    }

    public OrderResponse updateOrder(OrderId orderId, UpdateOrderCommand command) {
        Order order = findOrder(orderId);

        List<OrderItem> items = mapper.toDomainItems(command.items());
        order.replaceItems(items);

        Order savedOrder = orderRepository.save(order);
        return mapper.toResponse(savedOrder);
    }

    public OrderConfirmationResponse confirmOrder(OrderId orderId) {
        Order order = findOrder(orderId);

        // Prüfung gegen externes System
        if (externalOrderService.existsInExternalSystem(orderId)) {
            throw new OrderAlreadyExistsException(orderId);
        }

        // Produktinformationen laden und Hersteller ausgeben
        printProductManufacturers(order);

        // Stock reservieren - DIREKT über Product's Service!
        reserveStockForOrder(order);

        // Domain-Logik
        order.confirm();

        // Berechnung (Domain Service)
        OrderConfirmation confirmation = calculator.calculate(order);

        // Persistieren
        orderRepository.save(order);
        OrderConfirmation savedConfirmation = confirmationRepository.save(confirmation);

        // Benachrichtigung
        notificationService.sendOrderConfirmation(order.getCustomerId(), savedConfirmation);

        return mapper.toResponse(savedConfirmation);
    }

    private void printProductManufacturers(Order order) {
        // PSEUDO-CODE: Repräsentiert Logging/Audit-Action die in Produktion
        // über einen Logger oder AuditService erfolgen würde
        System.out.println("[ORDER CONFIRM] Produkte in Bestellung " + order.getId().value() + ":");
        for (OrderItem item : order.getItems()) {
            productInfoService.getProductInfo(item.getProductId())
                .ifPresentOrElse(
                    info -> System.out.printf("  - %s (Hersteller: %s)%n",
                        info.productName(), info.manufacturer()),
                    () -> System.out.printf("  - Produkt %d (Hersteller: unbekannt)%n",
                        item.getProductId().value())
                );
        }
    }

    /**
     * Reserviert Lagerbestand für alle Produkte der Bestellung.
     *
     * CROSS-DOMAIN CALL (GEWÜNSCHT für Demo-Zwecke):
     * Nutzt Product's ProductStockService direkt. Dies zeigt die pragmatische
     * Cross-Context-Kommunikation über Service Interfaces in Onion Architecture.
     * Product's InsufficientStockException wird durchgereicht - in Produktion
     * würde man diese ggf. in eine Order-spezifische Exception übersetzen.
     */
    private void reserveStockForOrder(Order order) {
        // PSEUDO-CODE: Logging-Action
        System.out.println("[ORDER SERVICE] Reserviere Lagerbestand für Order " + order.getId().value());

        for (OrderItem item : order.getItems()) {
            // Konvertiere Order.ProductId → Product.ProductId
            product.core.model.ProductId productId =
                product.core.model.ProductId.of(item.getProductId().value());

            // DIREKT Product's Service aufrufen!
            productStockService.reserveStock(productId, item.getQuantity());
        }
    }

    private Order findOrder(OrderId orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
