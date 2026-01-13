package product.framework.config;

import product.entity.model.Price;
import product.entity.model.Product;
import product.entity.model.ProductId;
import product.entity.model.ProductName;
import product.interfaceadapter.gateway.InMemoryProductGateway;
import product.usecase.boundary.input.GetProductInputBoundary;
import product.usecase.boundary.input.ReserveStockInputBoundary;
import product.usecase.boundary.output.ProductGateway;
import product.usecase.interactor.GetProductInteractor;
import product.usecase.interactor.ReserveStockInteractor;

/**
 * Configuration class for the Product module.
 */
public class ProductModuleConfiguration {

    private final ProductGateway productGateway;
    private final GetProductInputBoundary getProductUseCase;
    private final ReserveStockInputBoundary reserveStockUseCase;

    public ProductModuleConfiguration() {
        // Gateway
        this.productGateway = new InMemoryProductGateway();

        // Use Cases
        this.getProductUseCase = new GetProductInteractor(productGateway);
        this.reserveStockUseCase = new ReserveStockInteractor(productGateway);

        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        Product product1 = Product.create(
                new ProductId(1L),
                new ProductName("Laptop"),
                Price.of(999.99),
                100
        );

        Product product2 = Product.create(
                new ProductId(2L),
                new ProductName("Keyboard"),
                Price.of(79.99),
                200
        );

        Product product3 = Product.create(
                new ProductId(3L),
                new ProductName("Mouse"),
                Price.of(29.99),
                500
        );

        productGateway.save(product1);
        productGateway.save(product2);
        productGateway.save(product3);
    }

    public ProductGateway getProductGateway() {
        return productGateway;
    }

    public GetProductInputBoundary getGetProductUseCase() {
        return getProductUseCase;
    }

    public ReserveStockInputBoundary getReserveStockUseCase() {
        return reserveStockUseCase;
    }
}
