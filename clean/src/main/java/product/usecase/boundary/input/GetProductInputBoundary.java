package product.usecase.boundary.input;

import product.entity.model.Product;

/**
 * Input Boundary (Use Case Interface) for getting a Product.
 */
public interface GetProductInputBoundary {

    Product execute(Long productId);
}
