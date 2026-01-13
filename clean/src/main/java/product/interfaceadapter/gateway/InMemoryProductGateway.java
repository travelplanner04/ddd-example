package product.interfaceadapter.gateway;

import product.entity.model.Product;
import product.entity.model.ProductId;
import product.usecase.boundary.output.ProductGateway;

import java.util.*;

/**
 * In-memory implementation of the ProductGateway.
 */
public class InMemoryProductGateway implements ProductGateway {

    private final Map<ProductId, Product> products = new HashMap<>();

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }
}
