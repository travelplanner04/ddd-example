package product.core.exception;

import product.core.model.ProductId;

/**
 * Domain Exception - Produkt nicht gefunden.
 */
public class ProductNotFoundException extends RuntimeException {

    private final ProductId productId;

    public ProductNotFoundException(ProductId productId) {
        super("Product not found: " + productId.value());
        this.productId = productId;
    }

    public ProductId getProductId() {
        return productId;
    }
}
