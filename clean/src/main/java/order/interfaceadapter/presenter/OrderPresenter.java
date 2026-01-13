package order.interfaceadapter.presenter;

import order.usecase.dto.OrderConfirmationResponse;
import order.usecase.dto.OrderResponse;

/**
 * Presenter (Interface Adapter) for formatting Order responses.
 * In Clean Architecture, Presenters convert use case output into the format
 * suitable for the view or external system.
 */
public class OrderPresenter {

    public String presentOrder(OrderResponse order) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Order ===\n");
        sb.append("Order ID: ").append(order.orderId()).append("\n");
        sb.append("Customer: ").append(order.customerId()).append("\n");
        sb.append("Status: ").append(order.status()).append("\n");
        sb.append("Items:\n");

        order.items().forEach(item -> {
            sb.append("  - Product ").append(item.productId())
                    .append(": ").append(item.quantity())
                    .append(" x ").append(item.price())
                    .append(" = ").append(item.total())
                    .append("\n");
        });

        sb.append("Total: ").append(order.total()).append(" EUR\n");
        return sb.toString();
    }

    public String presentConfirmation(OrderConfirmationResponse confirmation) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Order Confirmation ===\n");
        sb.append("Order ID: ").append(confirmation.orderId()).append("\n");
        sb.append("Confirmed at: ").append(confirmation.confirmedAt()).append("\n");
        sb.append("Subtotal: ").append(confirmation.subtotal()).append(" EUR\n");
        sb.append("Tax (19%): ").append(confirmation.tax()).append(" EUR\n");
        sb.append("Shipping: ").append(confirmation.shipping()).append(" EUR\n");
        sb.append("Total: ").append(confirmation.total()).append(" EUR\n");
        return sb.toString();
    }
}
