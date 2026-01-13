package product.application.port.output;

import product.domain.model.Product;
import product.domain.model.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Output Port - Produkt laden.
 */
public interface LoadProductPort {

    Optional<Product> loadById(ProductId productId);

    List<Product> loadAll();
}
