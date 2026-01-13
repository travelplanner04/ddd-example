import order.entity.model.*;
import order.framework.config.OrderModuleConfiguration;
import order.interfaceadapter.controller.OrderController;
import order.interfaceadapter.presenter.OrderPresenter;
import order.usecase.boundary.output.OrderGateway;
import order.usecase.dto.OrderConfirmationResponse;
import order.usecase.dto.OrderItemRequest;
import order.usecase.dto.OrderResponse;
import order.usecase.dto.UpdateOrderRequest;
import product.framework.config.ProductModuleConfiguration;

import java.math.BigDecimal;
import java.util.List;

/**
 * Main application demonstrating Clean Architecture.
 *
 * Clean Architecture (Robert C. Martin) consists of concentric layers:
 *
 * 1. ENTITY (Enterprise Business Rules)
 *    - Contains domain models, value objects, and domain exceptions
 *    - No dependencies to outer layers
 *
 * 2. USE CASE (Application Business Rules)
 *    - Input Boundaries: Define what the application can do (interfaces)
 *    - Output Boundaries (Gateways): Define what the application needs (interfaces)
 *    - Interactors: Implement the use cases
 *    - DTOs: Data transfer between layers
 *
 * 3. INTERFACE ADAPTERS
 *    - Controllers: Convert external requests to use case input
 *    - Gateways: Implement output boundaries
 *    - Presenters: Format use case output for external display
 *
 * 4. FRAMEWORKS & DRIVERS
 *    - Web frameworks, databases, external services
 *    - Configuration
 *
 * The Dependency Rule: Dependencies only point inward.
 * Nothing in an inner layer can know about outer layers.
 */
public class CleanArchitectureApplication {

    public static void main(String[] args) {
        System.out.println("=== Clean Architecture Demo ===\n");

        // 1. Configure Product module
        ProductModuleConfiguration productConfig = new ProductModuleConfiguration();

        // 2. Configure Order module (depends on Product's use cases)
        OrderModuleConfiguration orderConfig = new OrderModuleConfiguration(
                productConfig.getProductGateway(),
                productConfig.getReserveStockUseCase()
        );

        // Get components
        OrderController controller = orderConfig.getOrderController();
        OrderPresenter presenter = orderConfig.getOrderPresenter();
        OrderGateway orderGateway = orderConfig.getOrderGateway();

        // 3. Create a sample order
        System.out.println("--- Creating Order ---");
        Order order = Order.create(new OrderId(1L), new CustomerId("CUST-001"));
        order.addItem(new ProductId(1L), new Quantity(2), Money.of(999.99));
        order.addItem(new ProductId(2L), new Quantity(1), Money.of(79.99));
        orderGateway.save(order);

        // 4. Get the order via Controller
        System.out.println("\n--- Getting Order ---");
        OrderResponse orderResponse = controller.getOrder(1L);
        System.out.println(presenter.presentOrder(orderResponse));

        // 5. Update the order
        System.out.println("\n--- Updating Order ---");
        UpdateOrderRequest updateRequest = new UpdateOrderRequest(List.of(
                new OrderItemRequest(1L, 1, BigDecimal.valueOf(999.99)),
                new OrderItemRequest(2L, 2, BigDecimal.valueOf(79.99)),
                new OrderItemRequest(3L, 3, BigDecimal.valueOf(29.99))
        ));
        OrderResponse updatedResponse = controller.updateOrder(1L, updateRequest);
        System.out.println(presenter.presentOrder(updatedResponse));

        // 6. Confirm the order
        System.out.println("\n--- Confirming Order ---");
        OrderConfirmationResponse confirmationResponse = controller.confirmOrder(1L);
        System.out.println(presenter.presentConfirmation(confirmationResponse));

        System.out.println("\n=== Demo Complete ===");
    }
}
