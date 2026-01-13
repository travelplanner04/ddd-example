package product.domain.exception;

import product.domain.model.ProductId;

/**
 * Domain Exception - Nicht genug Lagerbestand.
 */
public class InsufficientStockException extends RuntimeException {

    private final ProductId productId;
    private final int requested;
    private final int available;

    public InsufficientStockException(ProductId productId, int requested, int available) {
        super(String.format(
            "Insufficient stock for product %d: requested %d, available %d",
            productId.value(), requested, available
        ));
        this.productId = productId;
        this.requested = requested;
        this.available = available;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
