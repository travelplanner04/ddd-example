package product.adapter.input.rest;

import product.application.dto.ProductResponse;
import product.application.port.input.GetProductUseCase;
import product.domain.model.ProductId;

import java.util.List;

/**
 * Primary Adapter - REST Controller für Products.
 */
public class ProductController {

    private final GetProductUseCase getProductUseCase;

    public ProductController(GetProductUseCase getProductUseCase) {
        this.getProductUseCase = getProductUseCase;
    }

    // GET /products/{id}
    public ProductResponse getProduct(Long id) {
        return getProductUseCase.getProduct(ProductId.of(id));
    }

    // GET /products
    public List<ProductResponse> getAllProducts() {
        return getProductUseCase.getAllProducts();
    }
}
