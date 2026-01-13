package order.service;

import order.model.CustomerId;
import order.model.OrderConfirmation;

/**
 * Service Layer - Email Service.
 *
 * In Layered: Konkrete Klasse im Service Layer.
 */
public class EmailService {

    private final String smtpHost;
    private final int smtpPort;

    public EmailService(String smtpHost, int smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    public EmailService() {
        this("localhost", 25);
    }

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
