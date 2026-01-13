package order.application.service;

import order.application.dto.*;
import order.application.mapper.OrderMapper;
import order.application.port.input.*;
import order.application.port.output.*;
import order.domain.exception.OrderAlreadyExistsException;
import order.domain.exception.OrderNotFoundException;
import order.domain.model.*;
import order.domain.service.OrderConfirmationCalculator;
import product.application.port.input.ReserveStockUseCase;

import java.util.List;

/**
 * Application Service - orchestriert Use Cases.
 *
 * PRAGMATISCH: Nutzt Product's ReserveStockUseCase direkt!
 * Keine eigenen Ports/Adapter für Cross-Module-Kommunikation.
 */
public class OrderService implements GetOrderUseCase, UpdateOrderUseCase, ConfirmOrderUseCase {

    private final LoadOrderPort loadOrderPort;
    private final SaveOrderPort saveOrderPort;
    private final SaveConfirmationPort saveConfirmationPort;
    private final CheckOrderExistsPort checkOrderExistsPort;
    private final SendNotificationPort sendNotificationPort;
    private final LoadProductInfoPort loadProductInfoPort;
    private final ReserveStockUseCase reserveStockUseCase;  // Product's Use Case direkt!
    private final OrderConfirmationCalculator calculator;
    private final OrderMapper mapper;

    public OrderService(
            LoadOrderPort loadOrderPort,
            SaveOrderPort saveOrderPort,
            SaveConfirmationPort saveConfirmationPort,
            CheckOrderExistsPort checkOrderExistsPort,
            SendNotificationPort sendNotificationPort,
            LoadProductInfoPort loadProductInfoPort,
            ReserveStockUseCase reserveStockUseCase) {
        this.loadOrderPort = loadOrderPort;
        this.saveOrderPort = saveOrderPort;
        this.saveConfirmationPort = saveConfirmationPort;
        this.checkOrderExistsPort = checkOrderExistsPort;
        this.sendNotificationPort = sendNotificationPort;
        this.loadProductInfoPort = loadProductInfoPort;
        this.reserveStockUseCase = reserveStockUseCase;
        this.calculator = new OrderConfirmationCalculator();
        this.mapper = new OrderMapper();
    }

    @Override
    public OrderResponse getOrder(OrderId orderId) {
        Order order = loadOrder(orderId);
        return mapper.toResponse(order);
    }

    @Override
    public OrderResponse updateOrder(OrderId orderId, UpdateOrderCommand command) {
        Order order = loadOrder(orderId);

        List<OrderItem> items = mapper.toDomainItems(command.items());
        order.replaceItems(items);

        Order savedOrder = saveOrderPort.save(order);
        return mapper.toResponse(savedOrder);
    }

    @Override
    public OrderConfirmationResponse confirmOrder(OrderId orderId) {
        Order order = loadOrder(orderId);

        // Prüfung gegen externes System
        if (checkOrderExistsPort.existsInExternalSystem(orderId)) {
            throw new OrderAlreadyExistsException(orderId);
        }

        // Produktinformationen laden und Hersteller ausgeben
        printProductManufacturers(order);

        // Stock reservieren - DIREKT über Product's Use Case!
        reserveStockForOrder(order);

        // Domain-Logik
        order.confirm();

        // Berechnung (Domain Service)
        OrderConfirmation confirmation = calculator.calculate(order);

        // Persistieren
        saveOrderPort.save(order);
        OrderConfirmation savedConfirmation = saveConfirmationPort.save(confirmation);

        // Benachrichtigung
        sendNotificationPort.sendOrderConfirmation(order.getCustomerId(), savedConfirmation);

        return mapper.toResponse(savedConfirmation);
    }

    private void printProductManufacturers(Order order) {
        System.out.println("[ORDER CONFIRM] Produkte in Bestellung " + order.getId().value() + ":");
        for (OrderItem item : order.getItems()) {
            loadProductInfoPort.loadProductInfo(item.getProductId())
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
     * PRAGMATISCH: Nutzt Product's ReserveStockUseCase direkt.
     * Product's InsufficientStockException wird durchgereicht - keine Übersetzung!
     */
    private void reserveStockForOrder(Order order) {
        System.out.println("[ORDER SERVICE] Reserviere Lagerbestand für Order " + order.getId().value());

        for (OrderItem item : order.getItems()) {
            // Konvertiere Order.ProductId → Product.ProductId
            product.domain.model.ProductId productId =
                product.domain.model.ProductId.of(item.getProductId().value());

            // DIREKT Product's Use Case aufrufen!
            // InsufficientStockException wird durchgereicht
            reserveStockUseCase.reserveStock(productId, item.getQuantity());
        }
    }

    private Order loadOrder(OrderId orderId) {
        return loadOrderPort.loadById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
