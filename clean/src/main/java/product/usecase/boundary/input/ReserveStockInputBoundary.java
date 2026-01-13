package product.usecase.boundary.input;

/**
 * Input Boundary (Use Case Interface) for reserving product stock.
 * This interface is exposed to other modules (like Order) for pragmatic cross-module communication.
 */
public interface ReserveStockInputBoundary {

    void execute(Long productId, int quantity);
}
