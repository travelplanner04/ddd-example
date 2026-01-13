package product.application.mapper;

import product.application.dto.ProductResponse;
import product.core.model.Product;

import java.util.List;

/**
 * Mapper zwischen Domain und DTOs.
 */
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId().value(),
            product.getName().value(),
            product.getDescription(),
            product.getManufacturer(),
            product.getPrice().amount(),
            product.isAvailable()
        );
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
            .map(this::toResponse)
            .toList();
    }
}
