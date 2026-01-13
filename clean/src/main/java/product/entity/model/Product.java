package product.entity.model;

import product.entity.exception.InsufficientStockException;

/**
 * Aggregate Root representing a Product.
 */
public class Product {

    private final ProductId id;
    private final ProductName name;
    private final Price price;
    private int stock;

    private Product(ProductId id, ProductName name, Price price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static Product create(ProductId id, ProductName name, Price price, int initialStock) {
        if (initialStock < 0) {
            throw new IllegalArgumentException("Initial stock cannot be negative");
        }
        return new Product(id, name, price, initialStock);
    }

    public static Product reconstitute(ProductId id, ProductName name, Price price, int stock) {
        return new Product(id, name, price, stock);
    }

    public ProductId getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    /**
     * Reserves stock for an order. Throws exception if insufficient stock.
     */
    public void reserveStock(int quantity) {
        if (quantity > stock) {
            throw new InsufficientStockException(id, quantity, stock);
        }
        this.stock -= quantity;
    }

    /**
     * Releases previously reserved stock.
     */
    public void releaseStock(int quantity) {
        this.stock += quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name=" + name +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
