package order.framework.notification;

import order.entity.model.OrderConfirmation;
import order.usecase.boundary.output.NotificationGateway;

/**
 * Framework layer implementation for email notifications.
 */
public class EmailNotificationGateway implements NotificationGateway {

    @Override
    public void sendOrderConfirmation(OrderConfirmation confirmation, String customerEmail) {
        // Simulate sending email
        System.out.println("=== Sending Email ===");
        System.out.println("To: " + customerEmail);
        System.out.println("Subject: Order Confirmation #" + confirmation.getOrderId().getValue());
        System.out.println("Body: Your order has been confirmed!");
        System.out.println("  Subtotal: " + confirmation.getSubtotal());
        System.out.println("  Tax: " + confirmation.getTax());
        System.out.println("  Shipping: " + confirmation.getShipping());
        System.out.println("  Total: " + confirmation.getTotal());
        System.out.println("=====================");
    }
}
