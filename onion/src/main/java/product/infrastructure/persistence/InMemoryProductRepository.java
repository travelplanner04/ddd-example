package product.infrastructure.persistence;

import product.application.repository.ProductRepository;
import product.core.model.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Infrastructure - In-Memory Repository für Products.
 *
 * Speichert Product-Objekte direkt, damit Stock-Änderungen erhalten bleiben.
 */
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> database = new HashMap<>();

    public InMemoryProductRepository() {
        seedData();
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.ofNullable(database.get(productId.value()));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Product save(Product product) {
        database.put(product.getId().value(), product);
        return product;
    }

    private void seedData() {
        // Produkte MIT Lagerbestand!
        addProduct(1L, "Laptop Pro 15", "High-performance laptop", "Apple",
            new BigDecimal("1299.99"), true, 10);   // 10 auf Lager
        addProduct(2L, "Wireless Mouse", "Ergonomic wireless mouse", "Logitech",
            new BigDecimal("49.99"), true, 50);     // 50 auf Lager
        addProduct(3L, "USB-C Hub", "7-in-1 USB-C adapter", "Anker",
            new BigDecimal("79.99"), true, 25);     // 25 auf Lager
        addProduct(4L, "Mechanical Keyboard", "RGB mechanical keyboard", "Corsair",
            new BigDecimal("149.99"), true, 5);     // Nur 5 auf Lager!
        addProduct(5L, "Monitor 27\"", "4K UHD monitor", "Dell",
            new BigDecimal("399.99"), true, 8);     // 8 auf Lager
    }

    private void addProduct(Long id, String name, String description, String manufacturer,
                            BigDecimal price, boolean available, int stockQuantity) {
        Product product = Product.reconstitute(
            ProductId.of(id),
            ProductName.of(name),
            description,
            manufacturer,
            Price.of(price),
            available,
            stockQuantity
        );
        database.put(id, product);
    }
}
