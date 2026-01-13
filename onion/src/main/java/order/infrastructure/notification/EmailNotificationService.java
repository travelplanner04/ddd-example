package order.infrastructure.notification;

import order.application.service.NotificationService;
import order.core.model.CustomerId;
import order.core.model.OrderConfirmation;

/**
 * Infrastructure - Email Benachrichtigung.
 *
 * Implementiert NotificationService Interface aus Application.
 */
public class EmailNotificationService implements NotificationService {

    private final String smtpHost;
    private final int smtpPort;

    public EmailNotificationService(String smtpHost, int smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    public EmailNotificationService() {
        this("localhost", 25);
    }

    @Override
    public void sendOrderConfirmation(CustomerId customerId, OrderConfirmation confirmation) {
        String content = buildEmailContent(customerId, confirmation);

        System.out.printf(
            "[EMAIL] Sending to customer %s via %s:%d%n",
            customerId.value(),
            smtpHost,
            smtpPort
        );
        System.out.println("[EMAIL] Content: " + content);
    }

    private String buildEmailContent(CustomerId customerId, OrderConfirmation confirmation) {
        return String.format(
            """
            Sehr geehrter Kunde %s,

            Ihre Bestellung wurde bestätigt.

            Bestätigungs-Nr.: %d
            Bestellsumme: %.2f EUR
            MwSt.: %.2f EUR
            Versand: %.2f EUR
            ─────────────────
            Gesamtsumme: %.2f EUR

            Bestätigt am: %s

            Mit freundlichen Grüßen,
            Ihr Shop-Team
            """,
            customerId.value(),
            confirmation.getId(),
            confirmation.getTotalAmount().amount(),
            confirmation.getTaxAmount().amount(),
            confirmation.getShippingCost().amount(),
            confirmation.getGrandTotal().amount(),
            confirmation.getConfirmedAt()
        );
    }
}
