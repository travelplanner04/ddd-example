package product.domain.model;

import product.domain.exception.InsufficientStockException;

import java.util.Objects;

/**
 * Entity / Aggregate Root - Product.
 *
 * Enthält Domain-Logik für Lagerbestand.
 */
public class Product {

    private final ProductId id;
    private final ProductName name;
    private final String description;
    private final String manufacturer;
    private final Price price;
    private final boolean available;
    private int stockQuantity;  // Mutable: Lagerbestand ändert sich

    private Product(ProductId id, ProductName name, String description, String manufacturer,
                    Price price, boolean available, int stockQuantity) {
        this.id = Objects.requireNonNull(id, "Product ID cannot be null");
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
        this.description = description != null ? description : "";
        this.manufacturer = manufacturer != null ? manufacturer : "Unknown";
        this.price = Objects.requireNonNull(price, "Product price cannot be null");
        this.available = available;
        this.stockQuantity = stockQuantity;
    }

    // Factory Method für neue Produkte
    public static Product create(ProductId id, ProductName name, Price price) {
        return new Product(id, name, "", "Unknown", price, true, 0);
    }

    // Factory Method mit vollständigen Daten
    public static Product create(ProductId id, ProductName name, String description,
                                  String manufacturer, Price price, boolean available, int stockQuantity) {
        return new Product(id, name, description, manufacturer, price, available, stockQuantity);
    }

    // Factory Method für Rekonstitution aus Persistenz
    public static Product reconstitute(ProductId id, ProductName name, String description,
                                        String manufacturer, Price price, boolean available, int stockQuantity) {
        return new Product(id, name, description, manufacturer, price, available, stockQuantity);
    }

    // ==================== DOMAIN LOGIK ====================

    /**
     * Prüft ob genug Lagerbestand vorhanden ist.
     * REINE DOMAIN LOGIK - keine Abhängigkeiten!
     */
    public boolean hasEnoughStock(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }

    /**
     * Reserviert Lagerbestand für eine Bestellung.
     * DOMAIN LOGIK mit Geschäftsregel: Wirft Exception wenn nicht genug da.
     */
    public void reserveStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new InsufficientStockException(this.id, quantity, this.stockQuantity);
        }
        this.stockQuantity -= quantity;

        // PSEUDO-CODE: Repräsentiert Domain Event / Action die in Produktion
        // über einen EventPublisher publiziert würde: publish(new StockReservedEvent(...))
        System.out.printf("[PRODUCT DOMAIN] Stock reserved: %s, qty: %d, remaining: %d%n",
            name.value(), quantity, stockQuantity);
    }

    /**
     * Gibt reservierten Bestand zurück (z.B. bei Storno).
     */
    public void releaseStock(int quantity) {
        this.stockQuantity += quantity;

        // PSEUDO-CODE: Repräsentiert Domain Event / Action die in Produktion
        // über einen EventPublisher publiziert würde: publish(new StockReleasedEvent(...))
        System.out.printf("[PRODUCT DOMAIN] Stock released: %s, qty: %d, now: %d%n",
            name.value(), quantity, stockQuantity);
    }

    // Getters
    public ProductId getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Product[id=%s, name=%s, price=%s, available=%s]",
            id.value(), name.value(), price, available);
    }
}
