package product.application.repository;

import product.core.model.Product;
import product.core.model.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Repository Interface - Product.
 */
public interface ProductRepository {

    Optional<Product> findById(ProductId productId);

    List<Product> findAll();

    Product save(Product product);
}
