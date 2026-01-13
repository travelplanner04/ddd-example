package product.entity.exception;

import product.entity.model.ProductId;

/**
 * Exception thrown when a Product cannot be found.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(ProductId productId) {
        super("Product not found: " + productId);
    }
}
