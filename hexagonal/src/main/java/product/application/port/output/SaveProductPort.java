package product.application.port.output;

import product.domain.model.Product;

/**
 * Output Port - Produkt speichern.
 */
public interface SaveProductPort {

    Product save(Product product);
}
