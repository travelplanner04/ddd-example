package product.usecase.interactor;

import product.entity.exception.ProductNotFoundException;
import product.entity.model.Product;
import product.entity.model.ProductId;
import product.usecase.boundary.input.ReserveStockInputBoundary;
import product.usecase.boundary.output.ProductGateway;

/**
 * Interactor (Use Case Implementation) for reserving product stock.
 */
public class ReserveStockInteractor implements ReserveStockInputBoundary {

    private final ProductGateway productGateway;

    public ReserveStockInteractor(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public void execute(Long productId, int quantity) {
        ProductId id = new ProductId(productId);

        Product product = productGateway.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Domain logic: reserve stock (may throw InsufficientStockException)
        product.reserveStock(quantity);

        productGateway.save(product);

        System.out.println("Reserved " + quantity + " units of product " + productId);
    }
}
