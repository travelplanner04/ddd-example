package order.service;

import order.model.OrderId;

import java.util.Set;

/**
 * Service Layer - Externe API Integration.
 */
public class ExternalOrderApiService {

    private final String apiBaseUrl;
    private final String apiKey;

    // Simulierte externe Daten
    private final Set<Long> existingExternalOrders = Set.of(999L, 1000L, 1001L);

    public ExternalOrderApiService(String apiBaseUrl, String apiKey) {
        this.apiBaseUrl = apiBaseUrl;
        this.apiKey = apiKey;
    }

    public ExternalOrderApiService() {
        this("http://localhost:8080/api/v1", "dev-api-key");
    }

    public boolean existsInExternalSystem(OrderId orderId) {
        System.out.printf(
            "[EXTERNAL API] Checking order %d at %s%n",
            orderId.value(),
            apiBaseUrl
        );

        boolean exists = existingExternalOrders.contains(orderId.value());

        System.out.printf(
            "[EXTERNAL API] Order %d exists: %s%n",
            orderId.value(),
            exists
        );

        return exists;
    }
}
