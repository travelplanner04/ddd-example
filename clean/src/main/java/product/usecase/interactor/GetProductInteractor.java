package product.usecase.interactor;

import product.entity.exception.ProductNotFoundException;
import product.entity.model.Product;
import product.entity.model.ProductId;
import product.usecase.boundary.input.GetProductInputBoundary;
import product.usecase.boundary.output.ProductGateway;

/**
 * Interactor (Use Case Implementation) for getting a Product.
 */
public class GetProductInteractor implements GetProductInputBoundary {

    private final ProductGateway productGateway;

    public GetProductInteractor(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public Product execute(Long productId) {
        ProductId id = new ProductId(productId);

        return productGateway.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
