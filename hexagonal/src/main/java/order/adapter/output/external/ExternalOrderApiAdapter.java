package order.adapter.output.external;

import order.application.port.output.CheckOrderExistsPort;
import order.domain.model.OrderId;

import java.util.Set;

/**
 * Secondary Adapter - Externe API.
 *
 * Implementiert Prüfung gegen externes System.
 * In echter Anwendung: HTTP Client (RestTemplate, WebClient, etc.)
 */
public class ExternalOrderApiAdapter implements CheckOrderExistsPort {

    private final String apiBaseUrl;
    private final String apiKey;

    // Simulierte "externe" Datenbank für Demo
    private final Set<Long> existingExternalOrders = Set.of(999L, 1000L, 1001L);

    public ExternalOrderApiAdapter(String apiBaseUrl, String apiKey) {
        this.apiBaseUrl = apiBaseUrl;
        this.apiKey = apiKey;
    }

    // Convenience Constructor für lokale Entwicklung
    public ExternalOrderApiAdapter() {
        this("http://localhost:8080/api/v1", "dev-api-key");
    }

    @Override
    public boolean existsInExternalSystem(OrderId orderId) {
        // In echter Implementierung: HTTP Request an externes System
        // GET {apiBaseUrl}/orders/{orderId}/exists
        // Header: X-API-Key: {apiKey}

        System.out.printf(
            "[EXTERNAL API] Checking order %d at %s%n",
            orderId.value(),
            apiBaseUrl
        );

        // Simulierte Antwort
        boolean exists = existingExternalOrders.contains(orderId.value());

        System.out.printf(
            "[EXTERNAL API] Order %d exists: %s%n",
            orderId.value(),
            exists
        );

        return exists;
    }
}
