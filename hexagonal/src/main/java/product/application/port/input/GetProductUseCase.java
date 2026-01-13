package product.application.port.input;

import product.application.dto.ProductResponse;
import product.domain.model.ProductId;

import java.util.List;

/**
 * Input Port - Produkt abrufen.
 */
public interface GetProductUseCase {

    ProductResponse getProduct(ProductId productId);

    List<ProductResponse> getAllProducts();
}
