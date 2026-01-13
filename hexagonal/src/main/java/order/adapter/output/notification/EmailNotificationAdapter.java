package order.adapter.output.notification;

import order.application.port.output.SendNotificationPort;
import order.domain.model.CustomerId;
import order.domain.model.OrderConfirmation;

/**
 * Secondary Adapter - Email-Benachrichtigung.
 *
 * Implementiert den Notification Output Port.
 * In echter Anwendung: SMTP Client, SendGrid, etc.
 */
public class EmailNotificationAdapter implements SendNotificationPort {

    private final String smtpHost;
    private final int smtpPort;

    public EmailNotificationAdapter(String smtpHost, int smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    // Convenience Constructor für lokale Entwicklung
    public EmailNotificationAdapter() {
        this("localhost", 25);
    }

    @Override
    public void sendOrderConfirmation(CustomerId customerId, OrderConfirmation confirmation) {
        // In echter Implementierung: Email zusammenbauen und senden
        String emailContent = buildEmailContent(customerId, confirmation);

        // Simulierter Email-Versand
        System.out.printf(
            "[EMAIL] Sending to customer %s via %s:%d%n",
            customerId.value(),
            smtpHost,
            smtpPort
        );
        System.out.println("[EMAIL] Content: " + emailContent);
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
