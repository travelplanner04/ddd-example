package product.usecase.boundary.output;

import product.entity.model.Product;
import product.entity.model.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Output Boundary (Gateway Interface) for Product persistence.
 */
public interface ProductGateway {

    Optional<Product> findById(ProductId productId);

    List<Product> findAll();

    Product save(Product product);
}
