package order.usecase.boundary.output;

import order.entity.model.ProductId;

/**
 * Output Boundary (Gateway Interface) for loading product information.
 * Acts as an Anti-Corruption Layer between Order and Product bounded contexts.
 */
public interface ProductInfoGateway {

    ProductInfo loadProductInfo(ProductId productId);

    /**
     * Data structure for product information needed by the Order context.
     */
    record ProductInfo(Long productId, String name, String manufacturer) {}
}
