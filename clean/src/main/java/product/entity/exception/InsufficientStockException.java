package product.entity.exception;

import product.entity.model.ProductId;

/**
 * Exception thrown when there is insufficient stock for a product.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(ProductId productId, int requested, int available) {
        super("Insufficient stock for product " + productId + ": requested " + requested + ", available " + available);
    }
}
