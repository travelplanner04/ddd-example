package order.service;

import order.dto.*;
import order.exception.OrderAlreadyExistsException;
import order.exception.OrderNotFoundException;
import order.model.*;
import order.repository.OrderConfirmationRepository;
import order.repository.OrderRepository;

import java.util.List;

/**
 * Service Layer - Geschäftslogik.
 *
 * In Layered Architecture: Hängt direkt von Repositories ab.
 * Keine Abstraktion/Interfaces zwischen den Schichten.
 */
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    private final ExternalOrderApiService externalApiService;
    private final product.repository.ProductRepository productRepository;
    private final OrderConfirmationCalculator calculator;
    private final OrderMapper mapper;

    public OrderService(
            OrderRepository orderRepository,
            OrderConfirmationRepository confirmationRepository,
            EmailService emailService,
            ExternalOrderApiService externalApiService,
            product.repository.ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.confirmationRepository = confirmationRepository;
        this.emailService = emailService;
        this.externalApiService = externalApiService;
        this.productRepository = productRepository;
        this.calculator = new OrderConfirmationCalculator();
        this.mapper = new OrderMapper();
    }

    public OrderResponse getOrder(OrderId orderId) {
        Order order = findOrder(orderId);
        return mapper.toResponse(order);
    }

    public OrderResponse updateOrder(OrderId orderId, UpdateOrderRequest request) {
        Order order = findOrder(orderId);

        List<OrderItem> items = mapper.toDomainItems(request.items());
        order.replaceItems(items);

        Order savedOrder = orderRepository.save(order);
        return mapper.toResponse(savedOrder);
    }

    public OrderConfirmationResponse confirmOrder(OrderId orderId) {
        Order order = findOrder(orderId);

        // Prüfung gegen externes System
        if (externalApiService.existsInExternalSystem(orderId)) {
            throw new OrderAlreadyExistsException(orderId);
        }

        // Produktinformationen laden und Hersteller ausgeben
        printProductManufacturers(order);

        // ========== STOCK RESERVIERUNG ==========
        // LAYERED: Direkter Zugriff auf Product's Repository und Domain-Logik!
        // KEINE Abstraktion, KEINE Ports - direkte Abhängigkeit!
        reserveStockForOrder(order);

        // Geschäftslogik
        order.confirm();

        // Berechnung
        OrderConfirmation confirmation = calculator.calculate(order);

        // Persistieren
        orderRepository.save(order);
        OrderConfirmation savedConfirmation = confirmationRepository.save(confirmation);

        // Benachrichtigung
        emailService.sendOrderConfirmation(order.getCustomerId(), savedConfirmation);

        return mapper.toResponse(savedConfirmation);
    }

    private void printProductManufacturers(Order order) {
        // PSEUDO-CODE: Repräsentiert Logging/Audit-Action die in Produktion
        // über einen Logger oder AuditService erfolgen würde
        System.out.println("[ORDER CONFIRM] Produkte in Bestellung " + order.getId().value() + ":");
        for (OrderItem item : order.getItems()) {
            // Direkter Zugriff auf ProductRepository (Layered Style)
            product.model.ProductId productProductId =
                product.model.ProductId.of(item.getProductId().value());

            productRepository.findById(productProductId)
                .ifPresentOrElse(
                    p -> System.out.printf("  - %s (Hersteller: %s)%n",
                        p.getName().value(), p.getManufacturer()),
                    () -> System.out.printf("  - Produkt %d (Hersteller: unbekannt)%n",
                        item.getProductId().value())
                );
        }
    }

    /**
     * Reserviert Lagerbestand für alle Produkte der Bestellung.
     *
     * CROSS-DOMAIN CALL (GEWÜNSCHT für Demo-Zwecke):
     * LAYERED ARCHITECTURE - CROSS-MODULE INTERACTION:
     * - DIREKTER Zugriff auf Product's Repository (konkrete Klasse!)
     * - DIREKTER Aufruf von Product's Domain-Logik (reserveStock)
     * - KEINE Abstraktion, KEINE Ports, KEINE Interfaces!
     *
     * Dies demonstriert bewusst die direkte Kopplung in Layered Architecture.
     * Vorteile: Einfach, weniger Code
     * Nachteile: Enge Kopplung, schwer testbar, schwer austauschbar
     */
    private void reserveStockForOrder(Order order) {
        // PSEUDO-CODE: Logging-Action
        System.out.println("[ORDER SERVICE] Reserviere Lagerbestand für Order " + order.getId().value());

        for (OrderItem item : order.getItems()) {
            // Konvertiere Order.ProductId → Product.ProductId
            product.model.ProductId productProductId =
                product.model.ProductId.of(item.getProductId().value());

            // Lade Product DIREKT aus ProductRepository
            product.model.Product product = productRepository.findById(productProductId)
                .orElseThrow(() -> new RuntimeException(
                    "Product not found: " + item.getProductId().value()
                ));

            // RUFE PRODUCT'S DOMAIN-LOGIK DIREKT AUF!
            // Keine Übersetzung, keine Exception-Wrapping
            product.reserveStock(item.getQuantity());

            // Speichere DIREKT
            productRepository.save(product);

            System.out.printf("[ORDER SERVICE] Stock reserviert: %s, Menge: %d, Verbleibend: %d%n",
                product.getName().value(),
                item.getQuantity(),
                product.getStockQuantity()
            );
        }
    }

    private Order findOrder(OrderId orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
