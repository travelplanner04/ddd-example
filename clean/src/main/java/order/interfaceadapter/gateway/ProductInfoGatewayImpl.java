package order.interfaceadapter.gateway;

import order.entity.model.ProductId;
import order.usecase.boundary.output.ProductInfoGateway;
import product.entity.model.Product;
import product.usecase.boundary.output.ProductGateway;

/**
 * Implementation of ProductInfoGateway that loads product information from the Product module.
 * Acts as an Anti-Corruption Layer between Order and Product bounded contexts.
 */
public class ProductInfoGatewayImpl implements ProductInfoGateway {

    private final ProductGateway productGateway;

    public ProductInfoGatewayImpl(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public ProductInfo loadProductInfo(ProductId productId) {
        product.entity.model.ProductId productModuleId = new product.entity.model.ProductId(productId.getValue());

        Product product = productGateway.findById(productModuleId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        return new ProductInfo(
                product.getId().getValue(),
                product.getName().getValue(),
                "Default Manufacturer" // Product doesn't have manufacturer in this example
        );
    }
}
