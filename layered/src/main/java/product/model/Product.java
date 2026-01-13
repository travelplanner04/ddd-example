package product.model;

import product.exception.InsufficientStockException;

import java.util.Objects;

/**
 * Entity - Product.
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
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = description != null ? description : "";
        this.manufacturer = manufacturer != null ? manufacturer : "Unknown";
        this.price = Objects.requireNonNull(price);
        this.available = available;
        this.stockQuantity = stockQuantity;
    }

    public static Product create(ProductId id, ProductName name, Price price) {
        return new Product(id, name, "", "Unknown", price, true, 0);
    }

    public static Product reconstitute(ProductId id, ProductName name, String description,
                                        String manufacturer, Price price, boolean available, int stockQuantity) {
        return new Product(id, name, description, manufacturer, price, available, stockQuantity);
    }

    // ==================== DOMAIN LOGIK ====================

    /**
     * Prüft ob genug Lagerbestand vorhanden ist.
     */
    public boolean hasEnoughStock(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }

    /**
     * Reserviert Lagerbestand für eine Bestellung.
     * DOMAIN LOGIK: Wirft Exception wenn nicht genug da.
     */
    public void reserveStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new InsufficientStockException(this.id, quantity, this.stockQuantity);
        }
        this.stockQuantity -= quantity;
        System.out.printf("[PRODUCT] Stock reserved: %s, qty: %d, remaining: %d%n",
            name.value(), quantity, stockQuantity);
    }

    /**
     * Gibt reservierten Bestand zurück (z.B. bei Storno).
     */
    public void releaseStock(int quantity) {
        this.stockQuantity += quantity;
        System.out.printf("[PRODUCT] Stock released: %s, qty: %d, now: %d%n",
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
}
